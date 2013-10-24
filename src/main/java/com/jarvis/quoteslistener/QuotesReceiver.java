package com.jarvis.quoteslistener;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import progress.message.jclient.ConnectionFactory;

import com.jarvis.deserializer.Deserializer;
import com.jarvis.deserializer.IntegralDeserializerImpl;
import com.jarvis.order.OrderDisruptorWrapper;
import com.jarvis.order.OrderProducer;

public class QuotesReceiver {

	/*
	 * GLOBAL.IS.25.MV.TOIS.RATES.GRP77 GLOBAL.IS.25.MV.TOIS.RATES.GRP76
	 * GLOBAL.IS.25.MV.TOIS.RATES.GRP78
	 */
	private final String TOPIC_1_NAME = "GLOBAL.IS.25.MV.TOIS.RATES.GRP77";
	private final String TOPIC_2_NAME = "GLOBAL.IS.25.MV.TOIS.RATES.GRP76";
	private final String TOPIC_3_NAME = "GLOBAL.IS.25.MV.TOIS.RATES.GRP78";

	private final String SONIC_BROKER_CONNECTION_IP = "tcp://mvsonic:2507";
	
	private OrderProducer orderProducer = null;
	
	private Deserializer deserializer = null;
	
	 
	
	public QuotesReceiver(OrderProducer orderProducer, Deserializer deserializer) {
		super();
		this.orderProducer = orderProducer;
		this.deserializer = deserializer;
	}

	private Connection createConnectionToJMSBroker() throws JMSException {
		ConnectionFactory connectionFactory = new ConnectionFactory(SONIC_BROKER_CONNECTION_IP);		
		Connection connection = connectionFactory.createConnection();
		connection.start();
		return connection;
	}
	
	private Session createSessionToJMSBroker(Connection connection) throws JMSException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);		
		return session;
	}
	
	
	private MessageListener createMessageListener() {
		MessageListener topicMessageListener = new QuotesMessageListener(orderProducer, deserializer);
		return topicMessageListener;
	}
	
	private void setMessageListener(Session jmsSession, Destination topic, MessageListener messageListener) throws JMSException  {
		MessageConsumer msgConsumer = jmsSession.createConsumer(topic);
        msgConsumer.setMessageListener(messageListener);	
	}
	
	private Destination createTopic(Session session, String topicName) throws JMSException  {
		Destination topic = session.createTopic(topicName);
		return topic;
	}

	public void createAndSetJMSMessageListeners() throws JMSException  {				
			
			Connection jmsBrokerConnection = createConnectionToJMSBroker();
			Session jmsSession = createSessionToJMSBroker(jmsBrokerConnection);
			
			Destination topic1 = createTopic(jmsSession, TOPIC_1_NAME);
			MessageListener topic1Listener = createMessageListener();
	 		
			Destination topic2 = createTopic(jmsSession, TOPIC_2_NAME);
			MessageListener topic2Listener = createMessageListener();			
	 		
			Destination topic3 = createTopic(jmsSession, TOPIC_3_NAME);
			MessageListener topic3Listener = createMessageListener();

			setMessageListener(jmsSession, topic1, topic1Listener);
			setMessageListener(jmsSession, topic2, topic2Listener);
			setMessageListener(jmsSession, topic3, topic3Listener);	
						
	}

	public static void main(String[] args) throws JMSException {
		OrderDisruptorWrapper disruptorWrapper = new OrderDisruptorWrapper();
        OrderProducer producer = new OrderProducer(disruptorWrapper);
        disruptorWrapper.startTheDisruptor();
        
        Deserializer deserializer = new IntegralDeserializerImpl();
        
		QuotesReceiver quotesReceiver = new QuotesReceiver(producer, deserializer);
		quotesReceiver.createAndSetJMSMessageListeners();
	}

}
