package com.cubelogic.suspiciousDetector.execution;

import com.cubelogic.suspiciousDetector.behaviour.DeviateOrder;
import com.cubelogic.suspiciousDetector.behaviour.OppositeSideOrder;
import com.cubelogic.suspiciousDetector.behaviour.Testable;
import com.cubelogic.suspiciousDetector.execution.model.DetectionResult;
import com.cubelogic.suspiciousDetector.execution.model.TradeOrderTestPair;
import com.cubelogic.suspiciousDetector.model.Order;
import com.cubelogic.suspiciousDetector.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SuspiciousDetector implements SuspiciousDetectable {
    private static final List<Testable> TESTABLE_LIST = List.of(new OppositeSideOrder(), new DeviateOrder());

    public List<DetectionResult> test(final List<Trade> tradeList, final List<Order> orderList) {
        final var tradeOrderTestPairList = this.mapTradeOrderPair(tradeList, orderList);
        final List<DetectionResult> resultList = new ArrayList<>(tradeOrderTestPairList.size());
        for (var testable : TESTABLE_LIST) {
            tradeOrderTestPairList.stream().map(testable::test)
                    .filter(Predicate.not(Optional::isEmpty))
                    .forEach(x -> resultList.add(x.get()));
        }
        return resultList;
    }

    private List<TradeOrderTestPair> mapTradeOrderPair(final List<Trade> tradeList, final List<Order> orderList){
        final int MAX_SIZE = tradeList.size() * orderList.size();
        List<TradeOrderTestPair> resultList = new ArrayList<>(MAX_SIZE);
        for (var trade : tradeList) {
            for (var order : orderList) {
                if (trade.key() == order.key()) resultList.add(new TradeOrderTestPair(trade, order));
            }
        }
        return resultList.stream().toList(); //        return unmodifiable list
    }
}
