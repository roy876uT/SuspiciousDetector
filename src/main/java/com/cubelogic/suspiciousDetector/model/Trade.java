package com.cubelogic.suspiciousDetector.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public record Trade(long id, double price, double volume, Side side, LocalDateTime timestamp, long key) {
    public Trade {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(side);
    }

}
