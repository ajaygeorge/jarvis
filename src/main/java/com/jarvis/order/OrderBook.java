package com.jarvis.order;

import java.util.Comparator;
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
	private Comparator<Order> buyComparator = new Comparator<Order>() {

		@Override
		public int compare(Order o1, Order o2) {
			int compare = 0; 
			compare = o1.getPrice().compareTo(o2.getPrice());
			if (compare == 0) {
				compare = o1.getOrderTime().compareTo(o2.getOrderTime());
			}
			return compare;
		}
	};
	private SortedSet<Order> buyQueue = new TreeSet<>(buyComparator);
	private Comparator<Order> sellComparator = new Comparator<Order>() {

		@Override
		public int compare(Order o1, Order o2) {
			int compare = 0;
			compare = o2.getPrice().compareTo(o1.getPrice()) ;
			if (compare == 0) {
				compare = o1.getOrderTime().compareTo(o2.getOrderTime());
			}
			return compare ;
		}
	};
	private SortedSet<Order> sellQueue = new TreeSet<>(sellComparator);
	

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
			System.out.println(order.getPrice() +"/" + order.getVolume());
		}
		System.out.println("Offers");
		System.out.println("-------------------------");
		for (Order order : getSellQueue()) {
			System.out.println(order.getPrice() +"/" + order.getVolume());
		}
	}
}
