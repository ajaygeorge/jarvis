package com.jarvis.order;

import com.jarvis.matchingengine.MatchingEngine;
import com.jarvis.monitor.OrderStatsMonitor;
import com.lmax.disruptor.EventHandler;

public class OrderConsumer  {

	private OrderDisruptorWrapper orderDisruptorWrapper = null;
	
	private OrderBook orderBook = null;
	
	private MatchingEngine matchingEngine = null;
	
	private OrderStatsMonitor orderStatsMonitor = null;
	
	
	public OrderConsumer(OrderDisruptorWrapper disruptorWrapper, OrderBook orderBook, 
			MatchingEngine matchingEngine, OrderStatsMonitor orderStatsMonitor) {
    	this.orderDisruptorWrapper = disruptorWrapper;
    	this.orderBook = orderBook;
    	this.matchingEngine = matchingEngine;    	
    	this.orderStatsMonitor = orderStatsMonitor;
    }
	
    
    public void start() {
    	
    	EventHandler<Order> eventHandler = new EventHandler<Order>() {   
    		
            public void onEvent(final Order orderEvent, final long sequence, final boolean endOfBatch) throws Exception {
            	long startTime = System.nanoTime();
            	orderBook.submitOrder(orderEvent);
            	boolean isMatched = matchingEngine.match(orderBook);
            	long endTime = System.nanoTime();
            	long timeTaken = endTime - startTime;
            	orderStatsMonitor.monitorStats(orderEvent.getCurrencyPair(), orderEvent.getOrderAction().toString(), isMatched, timeTaken);
            	
            	
            }
            
        };
        
    	orderDisruptorWrapper.handleEvents(eventHandler);
    }

}
