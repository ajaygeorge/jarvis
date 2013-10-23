package com.jarvis.simulator;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.jarvis.order.Order;
import com.jarvis.order.OrderAction;
import com.jarvis.order.OrderSource;
import com.jarvis.simulator.jmx.OrderSimulatorAdmin;
import com.jarvis.simulator.jmx.OrderSimulatorAdminMBean;

public class OrderSimulator {
	
	
	private final int SLEEP_TIME = 2;
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

	public OrderSimulator() {
		init();
	}
	
	
	private void init() {
		availableCcyPairs.add("EUR/USD");
		availableCcyPairs.add("USD/JPY");

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
	
	private Order createOrderObject(String currencyPair, OrderAction orderAction, String price, Double volume) {
		Order order = new Order();
		order.setCurrencyPair(currencyPair);
		order.setOrderAction(orderAction);
		order.setOrderSource(OrderSource.LP);
		order.setPrice(new BigDecimal(price));
		order.setVolume(new BigInteger("0"));
		return order;
		
	}

	public void testRandomData() throws InterruptedException  {
		for(int i = 0 ; i < 200; i++) {
			String currencyPair = getRandomCurrency();
			OrderAction orderType = getRandomOrderType();
			String price = null;     
			double volume = 0.0;
			if(currencyPair.equals("EUR/USD")) {
				price = getRandomPriceForEURUSD();
				volume = getRandomVolumeForEURUSD();
			} else {
				price = getRandomPriceForUSDJPY();
				volume = getVolumeForUSDJPY();
			}
			Order order = createOrderObject(currencyPair, orderType, price, volume);
			System.out.println(currencyPair + "--" + price + "-" + orderType + "-" + volume + "-" + order);
			Thread.sleep(SLEEP_TIME);
		}
	}
	
	static public void main(String[] args) throws Exception {
		OrderSimulator randomOrdersSimulator = new OrderSimulator();
		
		OrderSimulatorAdminMBean orderSimulatorAdminMBean = new OrderSimulatorAdmin(randomOrdersSimulator);
		  MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	        ObjectName name = new ObjectName("OrderSimulator:type=Simulator"); 
	        
	        mbs.registerMBean(orderSimulatorAdminMBean, name); 
	        
	        Thread.currentThread().join();
		// randomOrdersSimulator.testRandomData();

	}   
	


}
