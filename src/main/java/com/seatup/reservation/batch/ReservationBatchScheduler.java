package com.seatup.reservation.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ReservationBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job expireReservationJob;

    public ReservationBatchScheduler(JobLauncher jobLauncher, @Qualifier("expireReservationJob") Job expireReservationJob) {
        this.jobLauncher = jobLauncher;
        this.expireReservationJob = expireReservationJob;
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void runExpireReservationJob() {
        log.info("[Batch-Reservation] 만료 예약 취소 배치 시작 - 실행 시간: {}", LocalDateTime.now());

        try {
            JobParameters params = new JobParametersBuilder()
                    .addLocalDateTime("now", LocalDateTime.now())
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(expireReservationJob, params);

            log.info("[Batch-Reservation] 배치 종료. 상태: {}, ID: {}",
                    execution.getStatus(), execution.getJobInstance().getInstanceId());
        } catch (Exception e) {
            log.error("[Batch-Reservation] 배치 실행 중 치명적 오류 발생: {}", e.getMessage(), e);
        }
    }
}
