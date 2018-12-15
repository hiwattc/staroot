package com.staroot.util.amqp;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    
    
    public void receiveMessage(Map<String, String> message) {
        System.out.println("Received(1) <" + message + ">");
        latch.countDown();
    }
    public void receiveMessage(String message) {
        System.out.println("Received(2) <" + message + ">");
        latch.countDown();
    }
    public void receiveMessage(byte[] message) {
        System.out.println("Received(3) <" + new String(message) + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}