package com.jarvis.order;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Order Book
 * 
 * @author Ajay George
 * 
 */
public class OrderBook {

	// Init impl of the queue. Probable replacement is a disruptor.
	private BlockingQueue<Order> buyQueue = new ArrayBlockingQueue<Order>(100);
	private BlockingQueue<Order> sellQueue = new ArrayBlockingQueue<Order>(100);

	public void submitOrder(Order order) throws OrderSubmitException {
		if (order.getOrderAction() == OrderAction.BUY) {
			getBuyQueue().offer(order);
		} else if (order.getOrderAction() == OrderAction.SELL) {
			getSellQueue().offer(order);
		}
	}

	public BlockingQueue<Order> getBuyQueue() {
		return buyQueue;
	}

	public BlockingQueue<Order> getSellQueue() {
		return sellQueue;
	}

	public int getPendingBuyOrders() {
		return getBuyQueue().size();
	}

	public int getPendingSellOrders() {
		return getSellQueue().size();
	}
}
