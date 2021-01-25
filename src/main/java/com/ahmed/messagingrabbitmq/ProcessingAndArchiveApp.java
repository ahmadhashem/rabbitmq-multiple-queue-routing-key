package com.ahmed.messagingrabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProcessingAndArchiveApp
{

    static final String topicExchangeName = "main-exchange";

    static final String processingQueueName = "processingQueue";
    static final String archiveQueueName = "archiveQueue";

    @Bean
    Queue processingQueue() {
        return new Queue(processingQueueName, false);
    }

    @Bean
    Queue archiveQueue() {
        return new Queue(archiveQueueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding processingBinding(Queue processingQueue, TopicExchange exchange) {
        return BindingBuilder.bind(processingQueue).to(exchange).with("processing.#");
    }

    @Bean
    Binding archiveBinding(Queue archiveQueue, TopicExchange exchange) {
        return BindingBuilder.bind(archiveQueue).to(exchange).with("#.archive.#");
    }

    @Bean ConnectionFactory connectionFactory()
    {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername("appuser");
        factory.setPassword("appuser");
        factory.setVirtualHost("demo");
        return factory;
    }

    @Bean
    SimpleMessageListenerContainer processingContainer(ConnectionFactory connectionFactory,
            MessageListenerAdapter processingListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(processingQueueName);
        container.setMessageListener(processingListener);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer archiveContainer(ConnectionFactory connectionFactory,
            MessageListenerAdapter archiveListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(archiveQueueName);
        container.setMessageListener(archiveListener);
        return container;
    }

    @Bean
    MessageListenerAdapter processingListener(ProcessingConsumer processingConsumer) {
        return new MessageListenerAdapter(processingConsumer, "onMessage");
    }

    @Bean
    MessageListenerAdapter archiveListener(ArchiveConsumer archiveConsumer) {
        return new MessageListenerAdapter(archiveConsumer, "onMessage");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProcessingAndArchiveApp.class, args).close();
    }

}