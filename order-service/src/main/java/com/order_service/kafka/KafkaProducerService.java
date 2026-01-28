package com.order_service.kafka;

import com.order_service.event.OrderPlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "order-events";

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderEvent(OrderPlacedEvent event){
        kafkaTemplate.send(TOPIC,event.getUserId(),event);
        System.out.println("Kafka event sent: Order ID "
                + event.getOrderId() + " for user " + event.getUserId());
    }
}
