package com.jarvis.monitor;

import com.lmax.disruptor.EventFactory;

public class MonEvent {
	
	private String currencyPair;
	
	private String orderType;
	
	private boolean isSuccess;
	
	private long timeTakenInMillis;

	public MonEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public long getTimeTakenInMillis() {
		return timeTakenInMillis;
	}

	public void setTimeTakenInMillis(long timeTakenInMillis) {
		this.timeTakenInMillis = timeTakenInMillis;
	}
	
	 public final static EventFactory<MonEvent> EVENT_FACTORY = new EventFactory<MonEvent>() {
	        public MonEvent newInstance() {
	            return new MonEvent();
	        }
	    };
	    
	

}
