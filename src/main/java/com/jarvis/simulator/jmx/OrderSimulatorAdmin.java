package com.jarvis.simulator.jmx;

import com.jarvis.simulator.OrderSimulator;

public class OrderSimulatorAdmin implements OrderSimulatorAdminMBean {

	private OrderSimulator orderSimulator = null;
	
	
	public OrderSimulatorAdmin(OrderSimulator orderSimulator) {
		super();
		this.orderSimulator = orderSimulator;
	}


	@Override
	public void startSimulation() throws Exception {
		orderSimulator.testRandomData();
		
	}

}
