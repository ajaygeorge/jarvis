package com.jarvis.monitor;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.lmax.disruptor.EventHandler;

public class MonEventProcessor implements MonEventProcessorMBean {

	private volatile Map<String, OrderStats> orderStatsMap = null;
	
	private final EventHandler<MonEvent> monEventHandler = new EventHandler<MonEvent>() { 
        public void onEvent(final MonEvent monEvent, final long sequence, final boolean endOfBatch) throws Exception {
        	monitorStats(monEvent);
        }
    };
    
	public MonEventProcessor(MonDisruptorWrapper monDisruptorWrapper) throws Exception {		
		orderStatsMap = new ConcurrentHashMap<String, OrderStats>();
		monDisruptorWrapper.handleEvents(monEventHandler);
		init();
	}
	
	
	private void exposeObjectOnJMX() throws Exception {		
		  MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	        ObjectName name = new ObjectName("OrderBook:type=MonEventProcessor"); 
	        
	        mbs.registerMBean(this, name); 
	        
	}
	private void init() throws Exception {
		exposeObjectOnJMX();
	}
	
		
	public void monitorStats(MonEvent monEvent) {
		String currencyPair = monEvent.getCurrencyPair();
		String orderType = monEvent.getOrderType();
		boolean isSuccess = monEvent.isSuccess();
		long timeTakenInMillis = monEvent.getTimeTakenInMillis();
		String orderStatsMapKeyValue = currencyPair+"-"+orderType;
		OrderStats stats = orderStatsMap.get(orderStatsMapKeyValue);		
		if(stats == null) {
			stats = new OrderStats();
			stats.setCurrencyPair(currencyPair);
			stats.setOrderType(orderType);				
			orderStatsMap.put(orderStatsMapKeyValue, stats);
		} 
		stats.setTotNoOfOrders(stats.getTotNoOfOrders() + 1);
		stats.setTotTimeTaken(stats.getTotTimeTaken() + timeTakenInMillis);
		if(! isSuccess) {
			stats.setTotNoOfFailOrders(stats.getTotNoOfFailOrders() + 1);
		}		
	}
	
	
	@Override
	public void printData() {
		Set<Entry<String, OrderStats>> entrySet = orderStatsMap.entrySet();
		System.out.println("CURRENCY_PAIR" + " ORDER_TYPE" + " TOTAL_ORDERS" + " SUCCESS_ORDERS" + " FAILURE_ORDERS" + " TOTAL_TIME_IN_MS" + " AVG TIME IN MS");
		for(Entry<String, OrderStats> entry : entrySet) {			
			OrderStats orderStats = entry.getValue();					
			String currencyPair = orderStats.getCurrencyPair();
			String orderType = orderStats.getOrderType();
			long totNoOfOrders = orderStats.getTotNoOfOrders();
			long totNoOfFailures = orderStats.getTotNoOfFailOrders();
			long totNoOfSuccOrders = totNoOfOrders - totNoOfFailures;
			long totTimeTaken = orderStats.getTotTimeTaken();
			double avgTime = totTimeTaken / totNoOfOrders;
			System.out.println(currencyPair + "            " + orderType + "           " + totNoOfOrders + "          " + totNoOfSuccOrders + "                     " + totNoOfFailures 
					+ "           " +  totTimeTaken + "              " + avgTime);
			
		}
		
	}
	
	
	
}
