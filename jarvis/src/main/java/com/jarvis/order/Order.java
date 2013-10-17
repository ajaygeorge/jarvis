package com.jarvis.order;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Order POJO
 * 
 * @author Ajay George
 *
 */
public class Order {

	private BigDecimal price;
	private BigInteger volume;
	private String currencyPair;
	private OrderAction orderAction;
	private OrderSource orderSource;
	
	public BigInteger getVolume() {
		return volume;
	}
	public void setVolume(BigInteger volume) {
		this.volume = volume;
	}
	public String getCurrencyPair() {
		return currencyPair;
	}
	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}
	public OrderAction getOrderAction() {
		return orderAction;
	}
	public void setOrderAction(OrderAction orderAction) {
		this.orderAction = orderAction;
	}
	public OrderSource getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(OrderSource orderSource) {
		this.orderSource = orderSource;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
