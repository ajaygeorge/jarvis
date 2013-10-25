package com.jarvis.simulator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.jarvis.order.OrderAction;
import com.jarvis.order.OrderProducer;
import com.jarvis.order.OrderSource;

public class OrderSimulator {
	
	
	private final int SLEEP_TIME = 1;
	
	private final String EUR_USD = "EUR/USD";
	private final String USD_JPY = "USD/JPY";
	
	private final double EUR_USD_START_PRICE = 1.2127;
	private final double EUR_USD_END_PRICE = 1.2134;

	private final double USD_JPY_START_PRICE = 98.55;
	private final double USD_JPY_END_PRICE = 98.59;

	private final int AVAILABLE_CCY_PAIRS_COUNT = 2;
	private final int ORDER_TYPES_COUNT = 2;

	private final List<String> availableCcyPairs = new ArrayList<String>(AVAILABLE_CCY_PAIRS_COUNT);    
	private final List<OrderAction> orderTypes = new ArrayList<OrderAction>(ORDER_TYPES_COUNT);
	
	private final List<Double> volumesForEURUSD = new ArrayList<Double>(4);
	
	private OrderProducer orderProducer = null;
	
	private Logger orderMessagesLogger = null;

	public OrderSimulator(OrderProducer orderProducer) {
		init();
		this.orderProducer = orderProducer;
		this.orderMessagesLogger = LogManager.getLogger("ORDER_MESSAGES_LOGGER");
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
		
			
	}
	
	

	private String getRandomPriceForEURUSD() {    	 
		return getRandomPrice(EUR_USD_START_PRICE, EUR_USD_END_PRICE);    	  
	}

	private String getRandomPriceForUSDJPY() {   	    	 
		return getRandomPrice(USD_JPY_START_PRICE, USD_JPY_END_PRICE);    	  
	}

	private Random priceRandomizer = new Random(new Object().hashCode());
	
	private String getRandomPrice(double startPrice, double endPrice){		
		double range = endPrice - startPrice;       
		double fraction = range * priceRandomizer.nextDouble();
		double randomNumber = fraction + startPrice;    
		NumberFormat priceNumberFormatter = NumberFormat.getInstance();
		priceNumberFormatter.setMaximumFractionDigits(4);            
		priceNumberFormatter.setGroupingUsed(false);
		
		return priceNumberFormatter.format(randomNumber);        
	}
	
	private String getRandomCurrency() {
		Random currencyRandomizer = new Random();    
		String randomCurrency = availableCcyPairs.get(currencyRandomizer.nextInt(AVAILABLE_CCY_PAIRS_COUNT));
		return randomCurrency;
	}

	private OrderAction getRandomOrderType() {    	
		Random orderTypeRandomizer = new Random();
		OrderAction orderType = orderTypes.get(orderTypeRandomizer.nextInt(ORDER_TYPES_COUNT));
		return orderType;
	}
	
	private double getRandomVolumeForEURUSD() {    	
		Random eurUsdVolumeRandomizer = new Random();  
		double volume = volumesForEURUSD.get(eurUsdVolumeRandomizer.nextInt(4));
		return volume;
	}
	
	private double getVolumeForUSDJPY() {
		return 100000;
	}
	
	 
	private void publishOrder(String currencyPair, OrderAction orderAction, double price, double volume) {		
		long volumeInLong = Double.valueOf(volume).longValue();
		orderMessagesLogger.info(" CURRENCY_PAIR " + currencyPair + " ORDER_ACTION " + orderAction + " PRICE " + price + " VOLUME " + volume);
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
		NumberFormat priceNumberFormatter = NumberFormat.getInstance();
		priceNumberFormatter.setMaximumFractionDigits(4);            
		priceNumberFormatter.setGroupingUsed(false);
		String priceAfterFormatting = priceNumberFormatter.format(price);		
		publishOrder(currencyPair, OrderAction.BUY, Double.parseDouble(priceAfterFormatting), volume);
	}
	
	public void submitSellOrder(String currencyPair, double price, long volume) {
		NumberFormat priceNumberFormatter = NumberFormat.getInstance();
		priceNumberFormatter.setMaximumFractionDigits(4);            
		priceNumberFormatter.setGroupingUsed(false);
		String priceAfterFormatting = priceNumberFormatter.format(price);		
		publishOrder(currencyPair, OrderAction.SELL, Double.parseDouble(priceAfterFormatting), volume);
	}
	
	


}
