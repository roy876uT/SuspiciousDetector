package com.cubelogic.suspiciousDetector.execution;

import com.cubelogic.suspiciousDetector.execution.model.DetectionResult;
import com.cubelogic.suspiciousDetector.model.Order;
import com.cubelogic.suspiciousDetector.model.Trade;

import java.util.List;

public interface SuspiciousDetectable {

    List<DetectionResult> test(List<Trade> tradeList, List<Order> orderList);
}
