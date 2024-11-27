package com.cubelogic.suspiciousDetector;

import com.cubelogic.suspiciousDetector.behaviour.OppositeSideOrder;
import com.cubelogic.suspiciousDetector.execution.SuspiciousDetectable;
import com.cubelogic.suspiciousDetector.execution.SuspiciousDetector;
import com.cubelogic.suspiciousDetector.execution.model.DetectionResult;
import com.cubelogic.suspiciousDetector.execution.model.SuspiciousReason;
import com.cubelogic.suspiciousDetector.model.Order;
import com.cubelogic.suspiciousDetector.model.Side;
import com.cubelogic.suspiciousDetector.model.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SpringBootTest
class SuspiciousDetectorApplicationTests {

	SuspiciousDetectable suspiciousDetectable = new SuspiciousDetector();
	@Test
	void contextLoads() {
	}

	final static LocalDateTime LESS_30_MIN_APART_START = LocalDateTime.of(2024, 10, 4,
			10, 20, 55);
	final static LocalDateTime LESS_30_MIN_APART_END = LocalDateTime.of(2024, 10, 4,
			10, 22, 55);

	final static LocalDateTime MORE_30_MIN_APART_START = LocalDateTime.of(2024, 10, 4,
			10, 1, 54);
	final static LocalDateTime MORE_30_MIN_APART_END = LocalDateTime.of(2024, 10, 4,
			10, 31, 55);
//
	@Test
	@DisplayName("30 min opposite before trade test")
	void min30oppositeBeforeTradeTest(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(LESS_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(LESS_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 1);
		Assertions.assertEquals(detectionResults.getFirst().suspiciousReason(), SuspiciousReason.OPPOSITE_ORDER_WITHIN_30_MIN);
	}

	@Test
	@DisplayName("30 min same side before trade test")
	void min30SameSideBeforeTradeTest(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(LESS_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(LESS_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 0);
	}

	@Test
	@DisplayName("opposite order appeared at 30 * 60+1 second before trade")
	void OppositeOrderAppearedAt30_60_1_SecondBeforeTrade(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(MORE_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 0);
	}

	@Test
	@DisplayName("multiple 30 min opposite before trade test")
	void multipleMin30oppositeBeforeTradeTest(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(LESS_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(LESS_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(LESS_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(LESS_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 3);
		Assertions.assertEquals(detectionResults.getFirst().suspiciousReason(), SuspiciousReason.OPPOSITE_ORDER_WITHIN_30_MIN);
	}

	@Test
	@DisplayName("multiple 30 over min opposite after trade test")
	void multipleOverMin30oppositeAfterTradeTest(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(MORE_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 0);
	}

	@Test
	@DisplayName("positive order price deviate with 10% of trade price")
	void PositiveOrderPriceDeviateWith10PctOfTradePrice(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(100).volume(volume).side(Side.BUY).timestamp(MORE_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(111).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(90).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(110).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(89).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 2);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.order().price() == 111).count(), 1);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.order().price() == 89).count(), 1);
		Assertions.assertIterableEquals(detectionResults.stream().map(DetectionResult::suspiciousReason).collect(Collectors.toSet()),
				Set.of(SuspiciousReason.ORDER_PRICE_HIGHER_OR_LOWER_THAN_10PCT));
	}

	@Test
	@DisplayName("negative order price deviate with 10% of trade price")
	void NegativeOrderPriceDeviateWith10PctOfTradePrice(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(-100).volume(volume).side(Side.BUY).timestamp(MORE_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(-111).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(-90).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(-110).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(-89).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 2);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.order().price() == -111).count(), 1);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.order().price() == -89).count(), 1);
		Assertions.assertIterableEquals(detectionResults.stream().map(DetectionResult::suspiciousReason).collect(Collectors.toSet()),
				Set.of(SuspiciousReason.ORDER_PRICE_HIGHER_OR_LOWER_THAN_10PCT));
	}

	@Test
	@DisplayName("zero order price not deviate with 10% of trade price")
	void zeroOrderPriceNotDeviateWith10PctOfTradePrice(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(0).volume(volume).side(Side.BUY).timestamp(MORE_30_MIN_APART_END).key(1).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(0).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(0).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(0).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(0).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
		);
		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 0);
	}

	@Test
	@DisplayName("mix of 2 test")
	void mixOf2Test(){
		long i=0; double price=100; double volume = 100; long key=0;
		var tradeList = List.of(
				Trade.builder().id(++i).price(100).volume(volume).side(Side.BUY).timestamp(MORE_30_MIN_APART_END).key(1).build()
				, Trade.builder().id(++i).price(++price).volume(volume).side(Side.BUY).timestamp(LESS_30_MIN_APART_END).key(2).build()
		);
		var orderList = List.of(
				Order.builder().id(++i).price(111).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(90).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(110).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(89).volume(volume).side(Side.SELL).timestamp(MORE_30_MIN_APART_START).key(1).build()
				, Order.builder().id(++i).price(++price).volume(volume).side(Side.SELL).timestamp(LESS_30_MIN_APART_START).key(2).build()
		);

		List<DetectionResult> detectionResults = suspiciousDetectable.test(tradeList, orderList);
		Assertions.assertEquals(detectionResults.size(), 3);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.order().price() == 111).count(), 1);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.order().price() == 89).count(), 1);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.suspiciousReason() == SuspiciousReason.ORDER_PRICE_HIGHER_OR_LOWER_THAN_10PCT).count(), 2);
		Assertions.assertEquals(detectionResults.stream().filter(n -> n.suspiciousReason() == SuspiciousReason.OPPOSITE_ORDER_WITHIN_30_MIN).count(), 1);
	}
}
