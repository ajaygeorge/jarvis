package com.jarvis.order;

/**
 * Buy Order
 * @author Ajay George
 *
 */
public class BuyOrder extends Order implements Comparable<BuyOrder> {

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


	@Override
	public int compareTo(BuyOrder o) {
		int compareTo = 0;
		int priceComparator = super.getPrice().compareTo(o.getPrice());
		if (priceComparator != 0 ) {
			compareTo = priceComparator;
		} else {
			compareTo = super.getOrderTime().compareTo(o.getOrderTime());
		}
		return compareTo;
	}
	
}
