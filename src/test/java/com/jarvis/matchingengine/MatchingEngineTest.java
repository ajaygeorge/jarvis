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
	public void testSamePriceBidOfferOrders() {
		//Set up buyOrder
		Order buyOrder = new Order();
		buyOrder.setCurrencyPair("EUR/USD");
		buyOrder.setOrderAction(OrderAction.BUY);
		buyOrder.setOrderSource(OrderSource.CUSTOMER);
		buyOrder.setPrice(BigDecimal.valueOf(1.3664));
		buyOrder.setVolume(BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = new Order();
		sellOrder.setCurrencyPair("EUR/USD");
		sellOrder.setOrderAction(OrderAction.SELL);
		sellOrder.setOrderSource(OrderSource.CUSTOMER);
		sellOrder.setPrice(BigDecimal.valueOf(1.3664));
		sellOrder.setVolume(BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(0, orderBook.getPendingBuyOrders());
		assertEquals(0, orderBook.getPendingSellOrders());
		//Add one more assert for last matched orders
	}

}
