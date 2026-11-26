package tn.esprit.marketplace.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "pawradise.events";
    public static final String QUEUE = "marketplace.adoption-completed";
    public static final String ROUTING_KEY = "adoption.completed";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue adoptionCompletedQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding adoptionCompletedBinding(Queue adoptionCompletedQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(adoptionCompletedQueue).to(eventsExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}