package com.seatup.queue.service;

import com.seatup.queue.dto.QueueTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class QueueService {

    private static final String QUEUE_KEY = "queue:performance:";
    private static final String ACTIVE_KEY = "active:performance:";
    private static final int MAX_ACTIVE_USERS = 50;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final String ENTER_SCRIPT =
            "local active_key = KEYS[1] " +
                    "local queue_key = KEYS[2] " +
                    "local token = ARGV[1] " +
                    "local max = tonumber(ARGV[2]) " +
                    "local score = tonumber(ARGV[3]) " +
                    "local activeCount = redis.call('SCARD', active_key) " +
                    "if activeCount < max then " +
                    "    redis.call('SADD', active_key, token) " +
                    "    return 1 " +
                    "else " +
                    "    redis.call('ZADD', queue_key, score, token) " +
                    "    return 0 " +
                    "end";

    private final StringRedisTemplate redisTemplate;

    /**
     * 대기열 진입 - 토큰 발급
     * @param performanceId
     * @param userId
     * @return
     */
    public QueueTokenResponse enterQueue(Long performanceId, Long userId) {
        String activeKey = ACTIVE_KEY + performanceId;
        String queueKey = QUEUE_KEY + performanceId;
        String token = userId + ":" + System.currentTimeMillis();
        double score = System.currentTimeMillis();

        RedisScript<Long> script = RedisScript.of(ENTER_SCRIPT, Long.class);
        Long result = redisTemplate.execute(
                script,
                List.of(activeKey, queueKey),
                token,
                String.valueOf(MAX_ACTIVE_USERS),
                String.valueOf((long) score)
        );

        if (result == 1L) {
            return new QueueTokenResponse(token, 0L, true);
        } else {
            Long rank = redisTemplate.opsForZSet().rank(queueKey, token);
            return new QueueTokenResponse(token, rank + 1, false);
        }
    }

    /**
     * 내 순번 조회
     * @param performanceId
     * @param token
     * @return
     */
    public Long getMyRank(Long performanceId, String token) {
        String queueKey = QUEUE_KEY + performanceId;
        Long rank = redisTemplate.opsForZSet().rank(queueKey, token);
        return rank == null ? -1L : rank;
    }

    /**
     * 입장 가능 여부 확인
     * @param performanceId
     * @param token
     * @return
     */
    public boolean isActivatable(Long performanceId, String token) {
        Long rank = getMyRank(performanceId, token);
        return rank != null && rank < MAX_ACTIVE_USERS;
    }

    /**
     * 대기열에서 제거
     * @param performanceId
     * @param token
     */
    public void removeFromQueue(Long performanceId, String token) {
        String queueKey = QUEUE_KEY + performanceId;
        String activeKey = ACTIVE_KEY + performanceId;

        redisTemplate.opsForSet().remove(activeKey, token);
        redisTemplate.opsForZSet().remove(queueKey, token);
        emitters.remove(token);

        // 대기열 맨 앞 사람 입장 허용
        promoteNext(performanceId);
        notifyAll(performanceId);
    }

    /**
     * 대기 중인 사용자 순번 업데이트
     * @param performanceId
     * @param token
     * @return
     */
    public SseEmitter subscribe(Long performanceId, String token) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 타임아웃 없이 연결 유지
        emitters.put(token, emitter);

        // 연결 끊기면 Map에서 제거
        emitter.onCompletion(() -> emitters.remove(token));
        emitter.onTimeout(() -> emitters.remove(token));
        emitter.onError(e -> emitters.remove(token));

        // 연결 직후 현재 순번 바로 전송
        try {
            Long rank = getMyRank(performanceId, token);
            boolean activatable = isActivatable(performanceId, token);
            emitter.send(SseEmitter.event()
                    .name("rank")
                    .data(Map.of("rank", rank + 1, "activatable", activatable)));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * 누군가 대기열에서 빠질 때 나머지 사람들 순번 업데이트
     * @param performanceId
     */
    public void notifyAll(Long performanceId) {
        emitters.forEach((token, emitter) -> {
            try {
                Long rank = getMyRank(performanceId, token);
                boolean activatable = isActivatable(performanceId, token);
                emitter.send(SseEmitter.event()
                        .name("rank")
                        .data(Map.of("rank", rank + 1, "activatable", activatable)));
            } catch (IOException e) {
                emitters.remove(token);
                emitter.completeWithError(e);
            }
        });
    }

    /**
     * 대기열 맨 첫 사용자 active로 이동
     * @param performanceId
     */
    public void promoteNext(Long performanceId) {
        String queueKey = QUEUE_KEY + performanceId;
        String activeKey = ACTIVE_KEY + performanceId;

        Long activeCount = redisTemplate.opsForSet().size(activeKey);
        if (activeCount == null || activeCount >= MAX_ACTIVE_USERS) return;

        // 대기열 맨 앞 사람 꺼내서 active로 이동
        Set<String> next = redisTemplate.opsForZSet().range(queueKey, 0, 0);
        if (next == null || next.isEmpty()) return;

        String nextToken = next.iterator().next();
        redisTemplate.opsForZSet().remove(queueKey, nextToken);
        redisTemplate.opsForSet().add(activeKey, nextToken);

        // 입장 가능 알림
        SseEmitter emitter = emitters.get(nextToken);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("rank")
                        .data(Map.of("rank", 0, "activatable", true)));
            } catch (IOException e) {
                emitters.remove(nextToken);
            }
        }
    }

}
