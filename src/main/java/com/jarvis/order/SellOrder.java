package com.jarvis.order;

public class SellOrder extends Order {

	@Override
	public OrderAction getOrderAction() {
		return OrderAction.SELL;
	}
	
	@Override
	public String toString() {
		return "SellOrder [OrderAction=" + getOrderAction()
				+ ", Volume=" + getVolume() + ", CurrencyPair="
				+ getCurrencyPair() + ", OrderSource=" + getOrderSource()
				+ ", Price=" + getPrice() + ", OrderTime="
				+ getOrderTime() + "]";
	}

}
