package com.jarvis.bootstrap;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.jarvis.deserializer.Deserializer;
import com.jarvis.deserializer.IntegralDeserializerImpl;
import com.jarvis.matchingengine.MatchingEngine;
import com.jarvis.monitor.MonDisruptorWrapper;
import com.jarvis.monitor.MonEventProcessor;
import com.jarvis.monitor.OrderStatsMonitor;
import com.jarvis.monitor.OrderStatsMonitorImpl;
import com.jarvis.order.OrderBook;
import com.jarvis.order.OrderConsumer;
import com.jarvis.order.OrderDisruptorWrapper;
import com.jarvis.order.OrderProducer;
import com.jarvis.quoteslistener.QuotesReceiver;
import com.jarvis.simulator.OrderSimulator;
import com.jarvis.simulator.jmx.OrderSimulatorAdmin;
import com.jarvis.simulator.jmx.OrderSimulatorAdminMBean;

public class Bootstrap {

	
	private static void exposeSimulatorMBeanOnJMX(OrderSimulatorAdminMBean orderSimulatorAdminMBean) {		
			try {
				
				MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
				ObjectName name = new ObjectName("OrderSimulator:type=Simulator"); 
				mbs.registerMBean(orderSimulatorAdminMBean, name);
			} catch(Exception e) { }

	
	}
	public static void main(String[] args) throws Exception {
		
		OrderBook orderBook = new OrderBook();
		MatchingEngine matchingEngine = new MatchingEngine();
		
		MonDisruptorWrapper monDisruptorWrapper = new MonDisruptorWrapper();
		MonEventProcessor monEventProcessor = new MonEventProcessor(monDisruptorWrapper);
		OrderStatsMonitor orderStatsMonitor = new OrderStatsMonitorImpl(monDisruptorWrapper);
		
		OrderDisruptorWrapper orderDisruptorWrapper = new OrderDisruptorWrapper();
		OrderProducer orderProducer = new OrderProducer(orderDisruptorWrapper);
		OrderConsumer orderConsumer = new OrderConsumer(orderDisruptorWrapper, orderBook, matchingEngine, orderStatsMonitor);
		
		Deserializer deserializer = new IntegralDeserializerImpl();
		
		QuotesReceiver quotesReceiver = new QuotesReceiver(orderProducer, deserializer);
		OrderSimulator orderSimulator = new OrderSimulator(orderProducer);
		OrderSimulatorAdminMBean orderSimulatorAdminMBean = new OrderSimulatorAdmin(orderSimulator, orderBook, quotesReceiver);
		
		exposeSimulatorMBeanOnJMX(orderSimulatorAdminMBean); 
		
		monDisruptorWrapper.startTheDisruptor();

		orderConsumer.start();
		orderDisruptorWrapper.startTheDisruptor();
		
		
	}
	
}
	