package com.godoy.delivery.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "delivery.exchange";

    public static final String PRODUCT_QUEUE = "delivery.products.queue";
    public static final String PRODUCT_DQL = "delivery.products.dlq";

    public static final String ROUTING_KEY_PRODUCT_CREATED = "product.created";
    public static final String ROUTING_KEY_PRODUCT_UPDATED = "product.updated";
    public static final String ROUTING_KEY_RESTAURANT_STATUS = "restaurant.status";

    @Bean
    public DirectExchange deliveryExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("delivery.dlx");
    }

    @Bean
    public Queue productQueue() {
        return QueueBuilder.durable(PRODUCT_QUEUE)
                .withArgument("x-dead-letter-exchange", "delivery.dlx")
                .withArgument("x-dead-letter-routing-key", "product.dead")
                .build();
    }

    @Bean
    public Queue productDlq() {
        return QueueBuilder.durable(PRODUCT_DQL).build();
    }

    @Bean
    public Binding bindingProductCreated() {
        return BindingBuilder.bind(productQueue())
                .to(deliveryExchange())
                .with(ROUTING_KEY_PRODUCT_CREATED);
    }

    @Bean
    public Binding bindingProductUpdated() {
        return BindingBuilder.bind(productQueue())
                .to(deliveryExchange())
                .with(ROUTING_KEY_PRODUCT_UPDATED);
    }

    @Bean
    public Binding bindingRestaurantStatus() {
        return BindingBuilder.bind(productQueue())
                .to(deliveryExchange())
                .with(ROUTING_KEY_RESTAURANT_STATUS);
    }

    @Bean
    public Binding bindingDlq() {
        return BindingBuilder.bind(productDlq())
                .to(deadLetterExchange())
                .with("product.dead");
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
