package com.cubelogic.suspiciousDetector.behaviour;

import com.cubelogic.suspiciousDetector.execution.model.DetectionResult;
import com.cubelogic.suspiciousDetector.execution.model.TradeOrderTestPair;

import java.util.Optional;

public sealed interface Testable permits DeviateOrder, OppositeSideOrder{
    Optional<DetectionResult> test(TradeOrderTestPair tradeOrderTestPair);
}
