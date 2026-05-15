package com.seatup.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueTokenResponse {
    private String token;
    private Long rank;
    private boolean immediate;
}
