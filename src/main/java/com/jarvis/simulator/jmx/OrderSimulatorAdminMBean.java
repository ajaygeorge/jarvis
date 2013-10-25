package com.jarvis.simulator.jmx;

public interface OrderSimulatorAdminMBean {
	
	public void startListeningToQuotes() throws Exception;
	
	public void startSimulation() throws Exception;
	
	public void stopSimulation() throws Exception;
	
	public String submitBuyOrder(String currencyPair, double price, long volume) throws Exception;
	
	public String submitSellOrder(String currencyPair, double price, long volume) throws Exception;
	
	public void orderBookStatus();

}
