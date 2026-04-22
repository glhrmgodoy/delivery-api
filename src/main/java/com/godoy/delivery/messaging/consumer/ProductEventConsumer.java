package com.godoy.delivery.messaging.consumer;

import com.godoy.delivery.config.RabbitMQConfig;
import com.godoy.delivery.messaging.event.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_QUEUE)
    public void consume(ProductEvent event) {
        log.info("Evento recebido - Tipo: {} | Produto: {} | Restaurante: {} | Status: {} | Preço: {}",
                event.getEventType(),
                event.getProductName(),
                event.getRestaurantName(),
                event.getStatus(),
                event.getPrice()
        );
    }
}
