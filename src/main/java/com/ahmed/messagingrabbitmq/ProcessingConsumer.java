package com.ahmed.messagingrabbitmq;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class ProcessingConsumer
{
    private CountDownLatch latch = new CountDownLatch(2);

    public void onMessage(String message) {
        System.out.println("Received [" + message + "] for processing");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}