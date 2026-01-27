package com.book_service.repository;

import com.book_service.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Book Repository - Data Access Layer
 *
 * Spring Data JPA auto-generates:
 * - findAll(), save(), delete(), findById()
 * - Custom queries via method names
 */
@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    // Query Method (Spring parses method name -> SQL)
    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("Select b FROM Book b WHERE b.price <= :maxPrice Order by b.price ASC")
    List<Book> findBooksByMaxPrice(@Param("maxPrice") BigDecimal maxPrice);

    // Pagination support
    Page<Book> findByStockGreaterThanEqual(int stock, Pageable pageable);
}

/**
 SELECT *
 FROM books
 WHERE stock >= ?
 LIMIT ? OFFSET ?

 Page 0 → records 1–10
 Page 1 → records 11–20
 Page 2 → records 21–30

 */
//<Prefix>By<Field><Condition><Modifier>
