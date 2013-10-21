package com.jarvis.matchingengine;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jarvis.order.BuyOrder;
import com.jarvis.order.Order;
import com.jarvis.order.OrderBook;
import com.jarvis.order.OrderSource;
import com.jarvis.order.SellOrder;

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
	public void testSameVolumeSamePrice() {
		//Set up buyOrder
		Order buyOrder = createBuyOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createSellOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(0, orderBook.getPendingBuyOrders());
		assertEquals(0, orderBook.getPendingSellOrders());
		//Add one more assert for last matched orders
	}

	@Test
	public void testBidVolumeGreaterSamePrice() {
		//Set up buyOrder
		Order buyOrder = createBuyOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(2000*1000*10));
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createSellOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(1, orderBook.getPendingBuyOrders());
		assertEquals(BigInteger.valueOf(1000*1000*10), orderBook.getBuyQueue().last().getVolume());
		assertEquals(0, orderBook.getPendingSellOrders());
		//Add one more assert for last matched orders
	}

	@Test
	public void testOfferVolumeGreaterSamePrice() {
		//Set up buyOrder
		Order buyOrder = createBuyOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createSellOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(2000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(0, orderBook.getPendingBuyOrders());
		assertEquals(BigInteger.valueOf(1000*1000*10), orderBook.getSellQueue().last().getVolume());
		assertEquals(1, orderBook.getPendingSellOrders());
		//Add one more assert for last matched orders
	}

	@Test
	public void testSameVolumeBidGreaterThanOffer() {
		//Set up buyOrder
		Order buyOrder = createBuyOrder(BigDecimal.valueOf(1.3667), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createSellOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(0, orderBook.getPendingBuyOrders());
		assertEquals(0, orderBook.getPendingSellOrders());
	}

	@Test
	public void testSameVolumeBidLesserThanOffer() {
		//Set up buyOrder
		Order buyOrder = createBuyOrder(BigDecimal.valueOf(1.3661), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder);
		//Set up sellOrder
		Order sellOrder = createSellOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		//THEN
		assertEquals(1, orderBook.getPendingBuyOrders());
		assertEquals(1, orderBook.getPendingSellOrders());
	}

	@Test
	public void testMultipleOrders() {
		//Set up buyOrder
		Order buyOrder1 = createBuyOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder1);
		Order buyOrder2 = createBuyOrder(BigDecimal.valueOf(1.3661), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(buyOrder2);
		//Set up sellOrder
		Order sellOrder = createSellOrder(BigDecimal.valueOf(1.3664), BigInteger.valueOf(1000*1000*10));
		orderBook.submitOrder(sellOrder);
		//WHEN
		matchingEngine.match(orderBook);
		assertEquals(1,	orderBook.getPendingBuyOrders());
		assertEquals(0, orderBook.getPendingSellOrders());
	}

	private Order createBuyOrder(BigDecimal price, BigInteger volume) {
		Order buyOrder = new BuyOrder();
		buyOrder.setCurrencyPair("EUR/USD");
		buyOrder.setOrderSource(OrderSource.CUSTOMER);
		buyOrder.setPrice(price);
		buyOrder.setVolume(volume);
		return buyOrder;
	}
	private Order createSellOrder(BigDecimal price, BigInteger volume) {
		Order buyOrder = new SellOrder();
		buyOrder.setCurrencyPair("EUR/USD");
		buyOrder.setOrderSource(OrderSource.CUSTOMER);
		buyOrder.setPrice(price);
		buyOrder.setVolume(volume);
		return buyOrder;
	}

	@After
	public void tearDown() {
		orderBook = null;
	}
}
