package com.jarvis.simulator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.QueueReceiver;

import com.jarvis.deserializer.Deserializer;
import com.jarvis.deserializer.IntegralDeserializerImpl;
import com.jarvis.order.OrderAction;
import com.jarvis.order.OrderConsumer;
import com.jarvis.order.OrderDisruptorWrapper;
import com.jarvis.order.OrderProducer;
import com.jarvis.order.OrderSource;
import com.jarvis.quoteslistener.QuotesMessageListener;
import com.jarvis.quoteslistener.QuotesReceiver;

public class OrderSimulator {
	
	
	private final int SLEEP_TIME = 1000;
	
	private final String EUR_USD = "EUR/USD";
	private final String USD_JPY = "USD/JPY";
	
	
	private final Random currencyRandomizer = new Random();    
	private final Random orderTypeRandomizer = new Random();
	private final Random priceRandomizer = new Random();
	private final Random eurUsdVolumeRandomizer = new Random();  

	private final double EUR_USD_START_PRICE = 98.55;
	private final double EUR_USD_END_PRICE = 98.59;

	private final double USD_JPY_START_PRICE = 1.2127;
	private final double USD_JPY_END_PRICE = 1.2134;

	private final int AVAILABLE_CCY_PAIRS_COUNT = 2;
	private final int ORDER_TYPES_COUNT = 2;

	private final List<String> availableCcyPairs = new ArrayList<String>(AVAILABLE_CCY_PAIRS_COUNT);    
	private final List<OrderAction> orderTypes = new ArrayList<OrderAction>(ORDER_TYPES_COUNT);
	
	private final List<Double> volumesForEURUSD = new ArrayList<Double>(4);
	
	private final NumberFormat priceNumberFormatter = NumberFormat.getInstance();
	
	private OrderProducer orderProducer = null;

	public OrderSimulator(OrderProducer orderProducer) {
		init();
		this.orderProducer = orderProducer;
	}
	
	
	private void init() {
		availableCcyPairs.add(EUR_USD);
		availableCcyPairs.add(USD_JPY);

		orderTypes.add(OrderAction.BUY);
		orderTypes.add(OrderAction.SELL);
		
		volumesForEURUSD.add(1.00E7);
		volumesForEURUSD.add(3.00E7);
		volumesForEURUSD.add(6.00E7);
		volumesForEURUSD.add(8.00E7);
		
		priceNumberFormatter.setMaximumFractionDigits(4);            
		priceNumberFormatter.setGroupingUsed(false);
		
	}

	private String getRandomPriceForEURUSD() {    	 
		return getRandomPrice(EUR_USD_START_PRICE, EUR_USD_END_PRICE);    	  
	}

	private String getRandomPriceForUSDJPY() {   	    	 
		return getRandomPrice(USD_JPY_START_PRICE, USD_JPY_END_PRICE);    	  
	}

	private String getRandomPrice(double startPrice, double endPrice){
		double range = endPrice - startPrice;       
		double fraction = range * priceRandomizer.nextDouble();
		double randomNumber = fraction + startPrice;    
		return priceNumberFormatter.format(randomNumber);        
	}
	
	private String getRandomCurrency() {
		String randomCurrency = availableCcyPairs.get(currencyRandomizer.nextInt(AVAILABLE_CCY_PAIRS_COUNT));
		return randomCurrency;
	}

	private OrderAction getRandomOrderType() {    	
		OrderAction orderType = orderTypes.get(orderTypeRandomizer.nextInt(ORDER_TYPES_COUNT));
		return orderType;
	}
	
	private double getRandomVolumeForEURUSD() {    	
		double volume = volumesForEURUSD.get(eurUsdVolumeRandomizer.nextInt(4));
		return volume;
	}
	
	private double getVolumeForUSDJPY() {
		return 100000;
	}
	
	 
	private void publishOrder(String currencyPair, OrderAction orderAction, double price, double volume) {		
		long volumeInLong = Double.valueOf(volume).longValue();
		orderProducer.publishOrder(currencyPair, orderAction, OrderSource.CUSTOMER, price, volumeInLong);				
	}

	public void generateRandomData() throws InterruptedException  {		
		String currencyPair = getRandomCurrency();
		OrderAction orderType = getRandomOrderType();
		String price = null;     
		double volume = 0.0;
		if(currencyPair.equals(EUR_USD)) {
			price = getRandomPriceForEURUSD();
			volume = getRandomVolumeForEURUSD();
		} else {
			price = getRandomPriceForUSDJPY();
			volume = getVolumeForUSDJPY();
		}
		publishOrder(currencyPair, orderType, Double.parseDouble(price), volume);
		Thread.sleep(SLEEP_TIME);
	}
	
	public void submitBuyOrder(String currencyPair, double price, long volume) {
		String priceAfterFormatting = priceNumberFormatter.format(price);		
		publishOrder(currencyPair, OrderAction.BUY, Double.parseDouble(priceAfterFormatting), volume);
	}
	
	
	


}
