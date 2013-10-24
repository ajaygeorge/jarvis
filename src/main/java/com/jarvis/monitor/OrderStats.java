package com.jarvis.monitor;

public class OrderStats {
	
	private String currencyPair;
	
	private String orderType;
	
	private long totNoOfOrders;
	
	private long totNoOfFailOrders;
	
	private long totTimeTaken;

	  

	public OrderStats() {
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

	public long getTotNoOfOrders() {
		return totNoOfOrders;
	}

	public void setTotNoOfOrders(long totNoOfOrders) {
		this.totNoOfOrders = totNoOfOrders;
	}

	public long getTotNoOfFailOrders() {
		return totNoOfFailOrders;
	}

	public void setTotNoOfFailOrders(long totNoOfFailOrders) {
		this.totNoOfFailOrders = totNoOfFailOrders;
	}

	public long getTotTimeTaken() {
		return totTimeTaken;
	}

	public void setTotTimeTaken(long totTimeTaken) {
		this.totTimeTaken = totTimeTaken;
	}

	@Override
	public String toString() {
		return "OrderStats [currencyPair=" + currencyPair + ", orderType="
				+ orderType + ", totNoOfOrders=" + totNoOfOrders
				+ ", totNoOfFailOrders=" + totNoOfFailOrders
				+ ", totTimeTaken=" + totTimeTaken + "]";
	}

	/* totNoOfSuccssOrders = totNoOfOrders - (minus) totNoOfFailOrders */
	
	
}
