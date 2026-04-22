package com.godoy.delivery.messaging.event;

import com.godoy.delivery.domain.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEvent {

    private UUID productId;
    private String productName;
    private BigDecimal price;
    private ProductStatus status;
    private String restaurantName;
    private String categoryName;
    private String eventType;
    private LocalDateTime occurredAt;
}
