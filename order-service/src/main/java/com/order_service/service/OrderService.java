package com.order_service.service;


import com.order_service.client.BookClient;
import com.order_service.dto.BookDTO;
import com.order_service.entity.Order;
import com.order_service.event.OrderPlacedEvent;
import com.order_service.kafka.KafkaProducerService;
import com.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookClient bookClient;

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Transactional
    public Order placeOrder(Long bookId, int quantity, String userId){
        //Step 1: Check book availability (SYNC REST call via WebClient)

        BookDTO book = bookClient.getBookById(bookId);
        if(book == null){
            throw new RuntimeException("Book not found with ID: " + bookId);
        }

        if(book.getStock() < quantity){
            throw new RuntimeException("Insufficient stock.Available: "+
                    book.getStock() + ", Requested: " + quantity);
        }

        // Step 2: Reduce stock
        boolean stockReduced = bookClient.reduceStock(bookId,quantity);
        if(!stockReduced){
            throw new RuntimeException("Failed to reduce stock");
        }

        // Step 3: Save order to database
        Order order = new Order();
        order.setBookId(bookId);
        order.setBookTitle(book.getTitle());
        order.setQuantity(quantity);
        order.setUserId(userId);
        order.setStatus("PLACED");
        Order savedOrder = orderRepository.save(order);

        // Step 4: Publish kafka event (Async)
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderId(savedOrder.getId());
        event.setUserId(userId);
        event.setBookId(bookId);
        event.setBookTitle(book.getTitle());
        event.setQuantity(quantity);
        kafkaProducer.sendOrderEvent(event);

        System.out.println("✅ Order placed successfully: "  + savedOrder.getId());
        return savedOrder;
    }

    public List<Order> getMyOrders(String userId){
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
}
