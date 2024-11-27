package com.cubelogic.suspiciousDetector.model;

public enum Side {
    BUY,
    SELL;

    public static boolean isOpposite(Trade trade, Order order){
        return trade.side() != order.side();
    }
}
