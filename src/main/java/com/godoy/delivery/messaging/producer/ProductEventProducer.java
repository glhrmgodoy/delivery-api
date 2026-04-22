package com.godoy.delivery.messaging.producer;

import com.godoy.delivery.config.RabbitMQConfig;
import com.godoy.delivery.messaging.event.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendProductCreated(ProductEvent event) {
        log.info("Publicando evento de criação do produto: {}", event.getProductId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_PRODUCT_CREATED, event);
    }

    public void sendProductUpdated(ProductEvent event) {
        log.info("Publicando evento de atualização do produto: {}",  event.getProductId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_PRODUCT_UPDATED, event);
    }

    public void sendRestaurantStatus(ProductEvent event) {
        log.info("Publicando evento de status do restaurante: {}", event.getRestaurantName());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_RESTAURANT_STATUS, event);
    }
}
