package com.jarvis.monitor;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;


public class MonDisruptorWrapper {
private ExecutorService exec = null;
    
    
    private Disruptor<MonEvent> disruptor = null;
    
    private RingBuffer<MonEvent> ringBuffer = null;
    
    public MonDisruptorWrapper() {
    	init();
    }
    
    private void init() {
    	exec = Executors.newCachedThreadPool();
    	disruptor = new Disruptor<MonEvent>(MonEvent.EVENT_FACTORY, 1024, exec);
    }
    
    
    public void startTheDisruptor() {
        ringBuffer = disruptor.start();
    }
    
    public void stopTheDisruptor() {
    	disruptor.shutdown();
        exec.shutdown();
    }
    
    @SuppressWarnings("unchecked")
	public void handleEvents(EventHandler<MonEvent> eventHandler) {
    	disruptor.handleEventsWith(eventHandler);    	
    }
    
    public long getNextSequenceNumberForProducer() {
    	long seq = ringBuffer.next();
    	return seq;
    }
    
    public MonEvent getTradeEventForSeqNum(long seqNum) {
    	MonEvent valueEvent =  ringBuffer.get(seqNum);
    	return valueEvent;
    }
    
    public void publishSeqNumToDisruptor(long seqNum) {    	
    	ringBuffer.publish(seqNum);
    }
}



 
