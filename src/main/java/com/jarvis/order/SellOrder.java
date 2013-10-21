package com.jarvis.order;

public class SellOrder extends Order implements Comparable<SellOrder> {

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


	@Override
	public int compareTo(SellOrder o) {
		int compareTo = 0;
		int priceComparator = o.getPrice().compareTo(super.getPrice());
		if (priceComparator != 0 ) {
			compareTo = priceComparator;
		} else {
			compareTo = super.getOrderTime().compareTo(o.getOrderTime());
		}
		return compareTo;
	}

}
