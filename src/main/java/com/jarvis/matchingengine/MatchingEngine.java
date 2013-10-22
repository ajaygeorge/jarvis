package com.jarvis.matchingengine;

import java.math.BigInteger;
import java.util.SortedSet;

import com.jarvis.order.Order;
import com.jarvis.order.OrderBook;

/**
 * Matching Engine
 * @author Ajay George
 *
 */
public class MatchingEngine {

	public void match(OrderBook orderBook) {
		SortedSet<Order> buyQueue = orderBook.getBuyQueue();
		SortedSet<Order> sellQueue = orderBook.getSellQueue();
		while (buyQueue.size()!=0 && sellQueue.size()!=0) {
			Order buy = buyQueue.last();
			Order sell = sellQueue.last();
			int priceComparator = buy.getPrice().compareTo(sell.getPrice());
			int volumeComparator = buy.getVolume().compareTo(sell.getVolume());
			if (priceComparator >= 0 && volumeComparator == 0) {
				//System.out.println("Order matched at " + sell.getPrice());
				buyQueue.remove(buy);
				sellQueue.remove(sell);
			} else if (priceComparator >=0 && volumeComparator > 0) {
				//System.out.println("Order matched at " + sell.getPrice());
				//System.out.println("Partial fill for " + sell.getVolume().divide(BigInteger.valueOf(1000*1000)));
				BigInteger currBuyVolume = buy.getVolume();
				buy.setVolume(currBuyVolume.subtract(sell.getVolume()));
				sellQueue.remove(sell);
			} else if (priceComparator >=0 && volumeComparator < 0) {
				//System.out.println("Order matched at " + sell.getPrice());
				//System.out.println("Partial fill for " + buy.getVolume().divide(BigInteger.valueOf(1000*1000)));
				BigInteger currSellVolume = sell.getVolume();
				sell.setVolume(currSellVolume.subtract(buy.getVolume()));
				buyQueue.remove(buy);
			} else if (priceComparator <0) {
				return;
			}
		}
	}

}
