package com.ahmed.messagingrabbitmq;

import org.springframework.stereotype.Component;

@Component
public class ArchiveConsumer
{
    public void onMessage(String message) {
        System.out.println("Received [" + message + "] for archiving");
    }
}