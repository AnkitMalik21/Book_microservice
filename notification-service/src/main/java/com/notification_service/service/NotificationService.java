package com.notification_service.service;

import com.notification_service.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @KafkaListener(topics = "order-events",groupId = "notification-group")
    public void handleOrderPlaced(OrderPlacedEvent event){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.info("🔔 ============================================");
        log.info("   ORDER NOTIFICATION RECEIVED");
        log.info("   Timestamp: {}", timestamp);
        log.info("   User: {}", event.getUserId());
        log.info("   Book: {} (ID: {})", event.getBookTitle(),event.getBookId());
        log.info("   Quantity: {}", event.getQuantity());
        log.info("============================================");

        //simulate email sending
        sendEmail(event);

        // Simulate SMS send
        sendSMS(event);
    }

    private void sendEmail(OrderPlacedEvent event) {
        System.out.println("\n📧 =============== SIMULATED EMAIL ===============");
        System.out.println("   To: " + event.getUserId() + "@bookstore.com");
        System.out.println("   Subject: Order Confirmation #" + event.getOrderId());
        System.out.println("   ---");
        System.out.println("   Dear " + event.getUserId() + ",");
        System.out.println("   ");
        System.out.println("   Thank you for your order!");
        System.out.println("   ");
        System.out.println("   Order Details:");
        System.out.println("   - Book: " + event.getBookTitle());
        System.out.println("   - Quantity: " + event.getQuantity());
        System.out.println("   - Order ID: " + event.getOrderId());
        System.out.println("   ");
        System.out.println("   Your order will be processed shortly.");
        System.out.println("   ");
        System.out.println("   Best regards,");
        System.out.println("   Bookstore Team");
        System.out.println("==================================================\n");
    }

    private void sendSMS(OrderPlacedEvent event) {
        System.out.println("📱 SMS sent to " + event.getUserId() +
                ": Order #" + event.getOrderId() + " confirmed!");
    }
}
