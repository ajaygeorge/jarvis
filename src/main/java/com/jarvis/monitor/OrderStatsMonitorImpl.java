package com.jarvis.monitor;





public class OrderStatsMonitorImpl implements OrderStatsMonitor {
	
	private MonDisruptorWrapper monDisruptorWrapper = null;
	
	public OrderStatsMonitorImpl(MonDisruptorWrapper monDisruptorWrapper) {
		this.monDisruptorWrapper = monDisruptorWrapper;
	}
	
	
	 
	@Override
	public void monitorStats(String currencyPair, String orderType,
			boolean isSuccess, long timeTakenInMillis) {
		   long seqNum = monDisruptorWrapper.getNextSequenceNumberForProducer();
           MonEvent monEvent =  monDisruptorWrapper.getTradeEventForSeqNum(seqNum);
           monEvent.setCurrencyPair(currencyPair);
           monEvent.setOrderType(orderType);
           monEvent.setSuccess(isSuccess);
           monEvent.setTimeTakenInMillis(timeTakenInMillis);
           monDisruptorWrapper.publishSeqNumToDisruptor(seqNum);		
	}


}
