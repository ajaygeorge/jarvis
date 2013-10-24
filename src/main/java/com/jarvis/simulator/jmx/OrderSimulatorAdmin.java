package com.jarvis.simulator.jmx;

import com.jarvis.simulator.OrderSimulator;

public class OrderSimulatorAdmin implements OrderSimulatorAdminMBean {

	private OrderSimulator orderSimulator = null;
	
	private volatile boolean runSimulation= false;
	
	private final int SIMULATOR_RUN_CHECK_COUNT = 1000;
	
	public OrderSimulatorAdmin(OrderSimulator orderSimulator) {
		super();
		this.orderSimulator = orderSimulator;
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

}
