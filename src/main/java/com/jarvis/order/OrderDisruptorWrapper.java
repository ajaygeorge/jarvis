package com.jarvis.order;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class OrderDisruptorWrapper  {
	
	private ExecutorService exec = null;
    
    private Disruptor<Order> disruptor = null;
    
    private RingBuffer<Order> ringBuffer = null;
    
    public OrderDisruptorWrapper() {
    	init();
    }
    
    private void init() {
    	exec = Executors.newCachedThreadPool();
    	disruptor = new Disruptor<Order>(Order.EVENT_FACTORY, 1024, exec);
    }
  
    public void startTheDisruptor() {
        ringBuffer = disruptor.start();
    }
    
    public void stopTheDisruptor() {
    	disruptor.shutdown();
        exec.shutdown();
    }
    
    @SuppressWarnings("unchecked")
	public void handleEvents(EventHandler<Order> eventHandler) {
    	disruptor.handleEventsWith(eventHandler);    	
    }
    
    public long getNextSequenceNumber() {
    	long seq = ringBuffer.next();
    	return seq;
    }
    
    public Order getEventForSequenceNumber(long seqNum) {
    	Order valueEvent =  ringBuffer.get(seqNum);
    	return valueEvent;
    }
    
    public void publishSeqNumToDisruptor(long seqNum) {    	
    	ringBuffer.publish(seqNum);
    }
    

}
