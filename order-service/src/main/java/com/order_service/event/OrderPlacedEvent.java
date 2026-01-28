package com.order_service.event;

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

/**
 *
In Java, objects exist in memory (RAM). When the program stops, those objects die.
Serialization is the process of converting an object's state
(its variables and data) into a byte stream (a sequence of 0s and 1s).
Once an object is serialized into bytes, you can:
Save it to a file (persistence).
Send it over a network (e.g., from a server to a client).
Store it in a database.
The reverse process—turning those bytes back into a live Java object—is called Deserialization.

 */
