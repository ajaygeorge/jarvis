package com.jarvis.monitor;

public interface OrderStatsMonitor {

	public void monitorStats(String currencyPair, String orderType, boolean isSuccess, long timeTakenInMillis);
	
}
