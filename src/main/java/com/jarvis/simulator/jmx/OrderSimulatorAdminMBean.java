package com.jarvis.simulator.jmx;

public interface OrderSimulatorAdminMBean {
	
	public void startSimulation() throws Exception;
	
	public void stopSimulation() throws Exception;
	
	public String submitBuyOrder(String currencyPair, double price, long volume) throws Exception;
	

}
