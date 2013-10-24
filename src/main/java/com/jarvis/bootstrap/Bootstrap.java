package com.jarvis.bootstrap;

import com.jarvis.deserializer.Deserializer;
import com.jarvis.deserializer.IntegralDeserializerImpl;
import com.jarvis.order.OrderConsumer;
import com.jarvis.order.OrderDisruptorWrapper;
import com.jarvis.order.OrderProducer;
import com.jarvis.quoteslistener.QuotesReceiver;

public class Bootstrap {

	public static void main(String[] args) throws Exception {
		
		OrderDisruptorWrapper orderDisruptorWrapper = new OrderDisruptorWrapper();
		OrderProducer orderProducer = new OrderProducer(orderDisruptorWrapper);
		OrderConsumer orderConsumer = new OrderConsumer(orderDisruptorWrapper);
		
		Deserializer deserializer = new IntegralDeserializerImpl();
		
		QuotesReceiver quotesReceiver = new QuotesReceiver(orderProducer, deserializer);
		
		
		orderDisruptorWrapper.startTheDisruptor();
		quotesReceiver.createAndSetJMSMessageListeners();
		
		
	}
	
}
