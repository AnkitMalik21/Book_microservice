package com.order_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id",nullable = false)
    private Long bookId;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @Column(nullable = false)
    private String status = "PLACED";

    @Column(name = "placed_at",nullable = false)
    private LocalDateTime placedAt = LocalDateTime.now();
}
