package com.jarvis.matchingengine;

import com.jarvis.order.Order;
import com.jarvis.order.OrderBook;

/**
 * Matching Engine
 * @author Ajay George
 *
 */
public class MatchingEngine {

	public void match(OrderBook orderBook) {
		Order buy = orderBook.getBuyQueue().poll();
		Order sell = orderBook.getSellQueue().poll();
		if (buy.getPrice().compareTo(sell.getPrice()) >= 0) {
			System.out.println("Order matched at " + sell.getPrice());
		}
	}


}
