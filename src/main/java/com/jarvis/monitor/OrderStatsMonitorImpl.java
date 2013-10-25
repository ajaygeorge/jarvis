package com.jarvis.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderStatsMonitorImpl implements OrderStatsMonitor {
	
	private MonDisruptorWrapper monDisruptorWrapper = null;
	
	public OrderStatsMonitorImpl(MonDisruptorWrapper monDisruptorWrapper) {
		this.monDisruptorWrapper = monDisruptorWrapper;
	}
	
	private Logger monitorLogger = LogManager.getLogger("PERF_LOGGER");
	 
	@Override
	public void monitorStats(String currencyPair, String orderType,
			boolean isSuccess, long timeInNanos) {
		   long seqNum = monDisruptorWrapper.getNextSequenceNumberForProducer();
           MonEvent monEvent =  monDisruptorWrapper.getTradeEventForSeqNum(seqNum);
           monEvent.setCurrencyPair(currencyPair);
           monEvent.setOrderType(orderType);
           monEvent.setSuccess(isSuccess);
           monEvent.setTimeTakenInNanos(timeInNanos);
           monitorLogger.info("CURRENCY_PAIR:" + currencyPair + " ORDER_TYPE:" + orderType + " IS_MATCHED:" + isSuccess + " TIME_IN_NANOS:" + timeInNanos);
           monDisruptorWrapper.publishSeqNumToDisruptor(seqNum);		
	}

}
