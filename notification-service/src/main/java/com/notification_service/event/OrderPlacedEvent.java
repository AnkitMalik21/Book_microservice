package com.notification_service.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderPlacedEvent implements Serializable {
    private Long orderId;
    private String userId;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
}
