package com.cubelogic.suspiciousDetector.behaviour;

import com.cubelogic.suspiciousDetector.execution.model.DetectionResult;
import com.cubelogic.suspiciousDetector.execution.model.SuspiciousReason;
import com.cubelogic.suspiciousDetector.execution.model.TradeOrderTestPair;
import com.cubelogic.suspiciousDetector.model.*;

import java.util.Optional;

public non-sealed class DeviateOrder implements Testable{
    private boolean isDeviate10Pct(Trade trade, Order order){
        var orderPrice = Math.abs(order.price());
        var tradePrice = Math.abs(trade.price());
        return orderPrice > tradePrice * 1.1 || orderPrice < tradePrice * 0.9;
    }

    public Optional<DetectionResult> test(TradeOrderTestPair tradeOrderTestPair){
        if (Side.isOpposite(tradeOrderTestPair.trade(), tradeOrderTestPair.order()) &&
                this.isDeviate10Pct(tradeOrderTestPair.trade(), tradeOrderTestPair.order())) {
            return Optional.of(new DetectionResult(SuspiciousReason.ORDER_PRICE_HIGHER_OR_LOWER_THAN_10PCT,
                    tradeOrderTestPair.trade(), tradeOrderTestPair.order()));
        };
        return Optional.empty();
    }

}
