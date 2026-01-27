package com.book_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Book Entity - Database Table Mapping
 *
 * JPA Annotations:
 * @Entity → Database table
 * @Table → Table name (defaults to class name)
 * @Id + @GeneratedValue → Primary key auto-increment
 * @Column → Column properties
 * @PrePersist → Before save hook
 */

@Entity
@Table(name = "books",indexes={
        @Index(name = "idx_isbn",columnList = "isbn" , unique = true),
        @Index(name="idx_title", columnList = "title")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false,length=255)
    @NotBlank(message="Title is required")
    @Size(max = 255,message="Title too long")
    private String title;

    @Column(nullable = false,length = 100)
    @NotBlank(message = "Author is required")
    @Size(max = 100)
    private String author;

    @Column(unique = true,nullable= false,length=13)
    @NotBlank(message="ISBN is required")
    @Pattern(regexp="\\d{10}|\\d{13}",message = "Invalid ISBN format")
    private String isbn;

    @Column(precision = 10,scale=2)
    @DecimalMin(value = "0.01",message = "Price must be positive")
    private BigDecimal price;

    @Column(nullable = false)
    @Min(value=0,message="Stock cannot be negative")
    private Integer stock = 0;

    @Column(name = "created_by",length = 50)
    private String createdBy;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate(){
        if(createdAt == null){
            createdAt = LocalDateTime.now();
        }
    }

}
