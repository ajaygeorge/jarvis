package com.jarvis.order;

import com.lmax.disruptor.EventHandler;

public class OrderConsumer  {

	private OrderDisruptorWrapper orderDisruptorWrapper = null;
	
	final EventHandler<Order> eventHandler = new EventHandler<Order>() {        
        public void onEvent(final Order orderEvent, final long sequence, final boolean endOfBatch) throws Exception {
        	System.out.println(" received order " + orderEvent);
        }
    };

    public OrderConsumer(OrderDisruptorWrapper disruptorWrapper) {
    	this.orderDisruptorWrapper = disruptorWrapper;
    	init();
    }
    
    private void init() {
    	orderDisruptorWrapper.handleEvents(eventHandler);
    }

}
