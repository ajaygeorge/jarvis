package com.jarvis.order.msgreceiver;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import java.nio.ByteBuffer;

import com.integral.is.message.MarketPrice;
import com.integral.is.message.MarketRate;
import com.integral.is.message.MarketRateC;
import com.integral.is.message.MarketRateSerializer;
import com.integral.is.message.V4MarketRateSerializer;

public class QuotesMessageListener implements MessageListener {

	private DeSerializer deSerializer = null;
	
	public QuotesMessageListener() {
		init();
	}
	
	private void init() {
		deSerializer = new DeSerializer();
	}
	
	private void log(String message) {
		System.out.println(message);
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage quotesTextMessage = (TextMessage) message;		
			String messageStr = quotesTextMessage.getText();
			log(quotesTextMessage.getText());		
			MarketRateC marketRateC = new MarketRateC();
			ByteBuffer bb = ByteBuffer.wrap(messageStr.getBytes()); 
			boolean isSerializationSuccessful = deSerializer.deserialize(bb , marketRateC);
			System.out.println(" isSerializationSuccessful " + isSerializationSuccessful);
			System.out.println(" marketRateC " + marketRateC);
		} catch(Exception e) {
			e.printStackTrace();
			log(e.getMessage());
		}
	}
		
	
	 

}
