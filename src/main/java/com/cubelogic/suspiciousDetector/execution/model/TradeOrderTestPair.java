package com.cubelogic.suspiciousDetector.execution.model;

import com.cubelogic.suspiciousDetector.model.Order;
import com.cubelogic.suspiciousDetector.model.Trade;

public record TradeOrderTestPair(Trade trade, Order order) {
}
