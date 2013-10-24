package com.jarvis.quoteslistener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.integral.is.message.MarketPrice;
import com.integral.is.message.MarketRate;
import com.jarvis.deserializer.Deserializer;
import com.jarvis.order.OrderAction;
import com.jarvis.order.OrderProducer;
import com.jarvis.order.OrderSource;


public class QuotesMessageListener implements MessageListener {

	private Deserializer deSerializer = null;
	
	private OrderProducer orderProducer = null;
	
	public QuotesMessageListener(OrderProducer orderProducer, Deserializer deSerializer) {
		this.orderProducer = orderProducer;
		this.deSerializer = deSerializer;
	}
	
	@Override
	public void onMessage(Message message) {
		try {			
			TextMessage quotesTextMessage = (TextMessage) message;		
			String quoteString = quotesTextMessage.getText();								
			MarketRate marketRate = deserializeQuoteString(quoteString);	
			convertQuotesToOrders(marketRate);
		} catch(Exception e) {
			e.printStackTrace();
			log(e.getMessage());
		}
	}
	
	private MarketRate deserializeQuoteString(String quoteString) {
		return deSerializer.deserialize(quoteString);
	}
	
	
	private void publishOrders(MarketRate marketRate, MarketPrice marketPrice, OrderAction orderAction) {
		String varCurrency = marketRate.getVariableCcy();
        String baseCurrency = marketRate.getBaseCcy();
        String currencyPair = baseCurrency + "/" + varCurrency;
        
        OrderSource orderSource = OrderSource.LP;
        double price = marketPrice.getRate();
        long volume = marketPrice.getLimit();
        
        orderProducer.publishOrder(currencyPair, orderAction, orderSource, price, volume);
	}
	
	private void convertQuotesToOrders(MarketRate rate) {
        int bidTierSize = rate.getBidTierSize();
        int offerTierSize = rate.getOfferTierSize();
        
        for(int i = 0 ; i < bidTierSize ; i++) {
            MarketPrice marketPrice = rate.getBidTier(i);                        
            OrderAction orderAction = OrderAction.BUY;
            publishOrders(rate, marketPrice, orderAction);
        }

        for(int i = 0 ; i < offerTierSize ; i++) {
        	MarketPrice marketPrice = rate.getOfferTier(i);               
            OrderAction orderAction = OrderAction.SELL;  
            publishOrders(rate, marketPrice, orderAction);
        }
        
	}
	

	private void log(String message) {
		System.out.println(message);
	}

}
