package com.ahmed.messagingrabbitmq;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ProcessingConsumer processingConsumer;

    public Runner(ProcessingConsumer receiver, RabbitTemplate rabbitTemplate) {
        this.processingConsumer = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(
                ProcessingAndArchiveApp.topicExchangeName,
                "processing.archive.baz", "A message for both processing and archive!");
        rabbitTemplate.convertAndSend(
                ProcessingAndArchiveApp.topicExchangeName,
                "processing.baz", "A message only for processing!");
        processingConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

}