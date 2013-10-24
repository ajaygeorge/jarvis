package com.jarvis.order;

import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Order Book
 * 
 * @author Ajay George
 * 
 */
public class OrderBook {

	// Init impl of the queue. Probable replacement is a disruptor.
	private SortedSet<Order> buyQueue = new TreeSet<Order>();
	private SortedSet<Order> sellQueue = new TreeSet<Order>();
	

	public void submitOrder(Order order) throws OrderSubmitException {
		if (order.getOrderAction() == OrderAction.BUY) {
			getBuyQueue().add(order);
		} else if (order.getOrderAction() == OrderAction.SELL) {
			getSellQueue().add(order);
		}
	}

	public SortedSet<Order> getBuyQueue() {
		return buyQueue;
	}

	public SortedSet<Order> getSellQueue() {
		return sellQueue;
	}

	public int getPendingBuyOrders() {
		return getBuyQueue().size();
	}

	public int getPendingSellOrders() {
		return getSellQueue().size();
	}

	public void status() {
		System.out.println("Bids"); 
		for (Order order : getBuyQueue()) {
			System.out.println(order.getPrice() +"/" + order.getVolume().divide(BigInteger.valueOf(1000*1000)));
		}
		System.out.println("Offers");
		System.out.println("-------------------------");
		for (Order order : getSellQueue()) {
			System.out.println(order.getPrice() +"/" + order.getVolume().divide(BigInteger.valueOf(1000*1000)));
		}
	}
}
