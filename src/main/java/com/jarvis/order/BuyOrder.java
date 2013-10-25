package com.jarvis.order;

/**
 * Buy Order
 * @author Ajay George
 *
 */
public class BuyOrder extends Order  {

	@Override
	public OrderAction getOrderAction() {
		return OrderAction.BUY;
	}

	@Override
	public String toString() {
		return "BuyOrder [OrderAction=" + getOrderAction()
				+ ", Volume=" + getVolume() + ", CurrencyPair="
				+ getCurrencyPair() + ", OrderSource=" + getOrderSource()
				+ ", Price=" + getPrice() + ", OrderTime="
				+ getOrderTime() + "]";
	}
	
}
