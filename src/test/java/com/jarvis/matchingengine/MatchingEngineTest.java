package com.jarvis.matchingengine;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.jarvis.order.Order;
import com.jarvis.order.OrderAction;
import com.jarvis.order.OrderBook;
import com.jarvis.order.OrderSource;
import static org.junit.Assert.*;

/**
 * Matching Engine Test
 * @author Ajay George
 *
 */
public class MatchingEngineTest {

	private MatchingEngine matchingEngine;
	private OrderBook orderBook;

	@Before
	public void setUp() throws Exception {
		matchingEngine = new MatchingEngine();
		orderBook = new OrderBook();
	}

	@Test
	public void testSameVolumeSamePriceBidOfferOrders() {
		//Set up buyOrder
		Order buyOrder = createOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10), OrderAction.BUY);
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10), OrderAction.SELL);
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(0, orderBook.getPendingBuyOrders());
		assertEquals(0, orderBook.getPendingSellOrders());
		//Add one more assert for last matched orders
	}

	@Test
	public void testSameVolumeBidGreaterThanOfferOrder() {
		//Set up buyOrder
		Order buyOrder = createOrder(BigDecimal.valueOf(1.3667), BigInteger.valueOf(1000*1000*10), OrderAction.BUY);
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10), OrderAction.SELL);
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(0, orderBook.getPendingBuyOrders());
		assertEquals(0, orderBook.getPendingSellOrders());
	}
	
	@Test
	public void testSameVolumeBidLesserThanOfferOrder() {
		//Set up buyOrder
		Order buyOrder = createOrder(BigDecimal.valueOf(1.3661), BigInteger.valueOf(1000*1000*10), OrderAction.BUY);
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10), OrderAction.SELL);
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(1, orderBook.getPendingBuyOrders());
		assertEquals(1, orderBook.getPendingSellOrders());
	}
	
	private Order createOrder(BigDecimal price, BigInteger volume, OrderAction orderAction) {
		Order buyOrder = new Order();
		buyOrder.setCurrencyPair("EUR/USD");
		buyOrder.setOrderAction(orderAction);
		buyOrder.setOrderSource(OrderSource.CUSTOMER);
		buyOrder.setPrice(price);
		buyOrder.setVolume(volume);
		return buyOrder;
	}
	
}
