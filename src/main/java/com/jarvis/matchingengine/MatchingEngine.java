package com.jarvis.matchingengine;

import java.math.BigInteger;
import java.util.SortedSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.jarvis.order.Order;
import com.jarvis.order.OrderBook;
import com.jarvis.order.OrderSource;

/**
 * Matching Engine
 * @author Ajay George
 *
 */
public class MatchingEngine {

	private Logger matchingEngineLogger = LogManager.getLogger(MatchingEngine.class);

	public boolean match(OrderBook orderBook) {
		SortedSet<Order> buyQueue = orderBook.getBuyQueue();
		SortedSet<Order> sellQueue = orderBook.getSellQueue();
		while (buyQueue.size()!=0 && sellQueue.size()!=0) {
			Order buy = buyQueue.last();
			Order sell = sellQueue.last();
			if (buy.getOrderSource() == OrderSource.LP && sell.getOrderSource() == OrderSource.LP) {
				return true;
			} else {
				return matchOrders(buyQueue, sellQueue, buy, sell);
			}
		}
		return false;
	}

	private boolean matchOrders(SortedSet<Order> buyQueue,
			SortedSet<Order> sellQueue, Order buy, Order sell) {
		int priceComparator = buy.getPrice().compareTo(sell.getPrice());
		int volumeComparator = buy.getVolume().compareTo(sell.getVolume());
		if (priceComparator >= 0 && volumeComparator == 0) {
			matchingEngineLogger.info("Order matched at " + sell.getPrice());
			buyQueue.remove(buy);
			sellQueue.remove(sell);
		} else if (priceComparator >=0 && volumeComparator > 0) {
			matchingEngineLogger.info("Order matched at " + sell.getPrice());
			matchingEngineLogger.info("Partial fill for " + sell.getVolume());
			BigInteger currBuyVolume = buy.getVolume();
			buy.setVolume(currBuyVolume.subtract(sell.getVolume()));
			sellQueue.remove(sell);
		} else if (priceComparator >=0 && volumeComparator < 0) {
			matchingEngineLogger.info("Order matched at " + sell.getPrice());
			matchingEngineLogger.info("Partial fill for " + buy.getVolume());
			BigInteger currSellVolume = sell.getVolume();
			sell.setVolume(currSellVolume.subtract(buy.getVolume()));
			buyQueue.remove(buy);
		} else if (priceComparator <0) {
			return false;
		}
		return true;
	}

}
