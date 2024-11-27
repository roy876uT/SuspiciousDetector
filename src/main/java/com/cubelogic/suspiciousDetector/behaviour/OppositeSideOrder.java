package com.cubelogic.suspiciousDetector.behaviour;

import com.cubelogic.suspiciousDetector.execution.model.DetectionResult;
import com.cubelogic.suspiciousDetector.execution.model.SuspiciousReason;
import com.cubelogic.suspiciousDetector.execution.model.TradeOrderTestPair;
import com.cubelogic.suspiciousDetector.model.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public non-sealed class OppositeSideOrder implements Testable{

    private static final int WINDOW_TIMEOUT_SECOND = 30 * 60;
    public Optional<DetectionResult> test(TradeOrderTestPair tradeOrderTestPair){
        if (Side.isOpposite(tradeOrderTestPair.trade(), tradeOrderTestPair.order()) &&
                Duration.between(tradeOrderTestPair.order().timestamp(), tradeOrderTestPair.trade().timestamp()).toSeconds() < WINDOW_TIMEOUT_SECOND) {
            return Optional.of(new DetectionResult(SuspiciousReason.OPPOSITE_ORDER_WITHIN_30_MIN,
                    tradeOrderTestPair.trade(), tradeOrderTestPair.order()));
        }
        return Optional.empty();
    }

}
