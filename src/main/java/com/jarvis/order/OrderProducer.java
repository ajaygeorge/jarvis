package com.jarvis.order;

import java.math.BigDecimal;
import java.math.BigInteger;


public class OrderProducer {

	private OrderDisruptorWrapper disruptorWrapper = null;

	public OrderProducer(OrderDisruptorWrapper disruptorWrapper) {
		this.disruptorWrapper = disruptorWrapper;
	}

	public void publishOrder(String currencyPair, OrderAction orderAction, 
							 OrderSource orderSource, double price, long volume) {
		
		
		if (currencyPair.equals("EUR/USD")) {		
				long seqNum = disruptorWrapper.getNextSequenceNumber();
				Order orderEvent = disruptorWrapper.getEventForSequenceNumber(seqNum);
				orderEvent.setCurrencyPair(currencyPair);
				orderEvent.setOrderAction(orderAction);
				orderEvent.setOrderSource(orderSource);		
				orderEvent.setPrice(new BigDecimal(price).setScale(4, BigDecimal.ROUND_HALF_UP));
				orderEvent.setVolume( new BigInteger(Long.toString(volume)));
				disruptorWrapper.publishSeqNumToDisruptor(seqNum);		
		}
	}
	
}
