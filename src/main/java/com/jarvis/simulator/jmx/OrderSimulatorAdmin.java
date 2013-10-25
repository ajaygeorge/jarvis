package com.jarvis.simulator.jmx;

import com.jarvis.order.OrderBook;
import com.jarvis.quoteslistener.QuotesReceiver;
import com.jarvis.simulator.OrderSimulator;

public class OrderSimulatorAdmin implements OrderSimulatorAdminMBean {

	private OrderSimulator orderSimulator = null;
	
	private volatile boolean runSimulation= false;
	
	private final int SIMULATOR_RUN_CHECK_COUNT = 1000;

	private OrderBook orderbook;
	
	private QuotesReceiver quotesReceiver = null;
	
	
	public OrderSimulatorAdmin(OrderSimulator orderSimulator, OrderBook orderbook, QuotesReceiver quotesReceiver) {
		super();
		this.orderSimulator = orderSimulator;
		this.orderbook = orderbook;
		this.quotesReceiver = quotesReceiver;
	}


	@Override
	public void startSimulation() throws Exception {
		runSimulation = true;
		int count = 0;
		while(true) {
			if(count % SIMULATOR_RUN_CHECK_COUNT == 0) {
				if(! runSimulation) {
					break;
				}
			}  
			orderSimulator.generateRandomData();
		}
	}


	@Override
	public void stopSimulation() throws Exception {
		runSimulation = false;
	}


	@Override
	public String submitBuyOrder(String currencyPair, double price, long volume) throws Exception {
		orderSimulator.submitBuyOrder(currencyPair, price, volume);
		return "ORDER_SUBMITTED";
	}
	
	@Override
	public String submitSellOrder(String currencyPair, double price, long volume) throws Exception {
		orderSimulator.submitSellOrder(currencyPair, price, volume);
		return "ORDER_SUBMITTED";
	}


	@Override
	public void orderBookStatus() {
		orderbook.status();
	}


	@Override
	public void startListeningToQuotes() throws Exception {
		quotesReceiver.createAndSetJMSMessageListeners();		
	}
	
	

}
