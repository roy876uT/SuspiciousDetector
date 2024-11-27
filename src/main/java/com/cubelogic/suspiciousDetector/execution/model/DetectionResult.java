package com.cubelogic.suspiciousDetector.execution.model;

import com.cubelogic.suspiciousDetector.model.Order;
import com.cubelogic.suspiciousDetector.model.Trade;

public record DetectionResult(SuspiciousReason suspiciousReason, Trade trade, Order order) {

}
