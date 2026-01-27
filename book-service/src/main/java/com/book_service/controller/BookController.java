package com.book_service.controller;

import com.book_service.entity.Book;
import com.book_service.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Book Controller - REST API Endpoints
 *
 * Security Model:
 * ✅ Gateway validates JWT
 * ✅ Passes X-User-Id header
 * ✅ Service trusts Gateway (stateless)
 */


@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*") // Allow frontend call
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final HttpServletRequest request;

    /**
     * Health Check
     * */

    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> health(){
        Map<String,String> health = new HashMap<>();
        health.put("status","UP");
        health.put("service","book-service");
        return ResponseEntity.ok(health);
    }

    /**
     * Create Book - ADMIN ONLY ✅
     * POST /books
     */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // only ADMIN can create
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book){
        try{
            String userId = getUserIdFromHeader();
            Book savedBook = bookService.createBook(book,userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get All Books - USER & ADMIN ✅
     * GET /books
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')") // Both can view
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Get Books with Stock - USER & ADMIN ✅
     * GET /books/in-stock?page=0&size=10
     */
    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('USER','ADMIN')") // Both can view
    public ResponseEntity<?> getBooksInStock(Pageable pageable){
        return ResponseEntity.ok(bookService.getBooksWithStocks(pageable));
    }

    /**
     * Get Book by ID - USER & ADMIN ✅
     * GET /books/1
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * Update Book - ADMIN ONLY ✅
     * PUT /books/1
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // only admin can update
    public ResponseEntity<?> updateBook(@PathVariable Long id,@Valid @RequestBody Book bookDetails){
        try{
            String userId = getUserIdFromHeader();
            Book updated = bookService.updateBook(id,bookDetails,userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    /**
     * Reduce Stock - ADMIN ONLY ✅
     * POST /books/{id}/reduce-stock?quantity=2
     * (Order Service should call with service-to-service token)
     */
    @PostMapping("/{id}/reduce-stock")
    @PreAuthorize("hasRole('ADMIN')") // Secure stock modification
    public ResponseEntity<Map<String,Boolean>> reduceStock(@PathVariable Long id,@RequestParam int quantity){
        boolean success = bookService.reduceStock(id,quantity);
        return ResponseEntity.ok(Map.of("success",success));
    }

    /**
     * Delete Book - ADMIN ONLY ✅
     * DELETE /books/1
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") //Only ADMIN can delete
    public ResponseEntity<Map<String,String>> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.ok(Map.of("message","Book deleted successfully"));
    }

    private String getUserIdFromHeader(){
        String userId = request.getHeader("X-User-Id");
        if(userId ==null  || userId.isEmpty()){
            throw new RuntimeException("User Id header missing - Gateway issue");
        }
        return userId;
    }


}
