package com.book_service.service;

import com.book_service.entity.Book;
import com.book_service.exception.BookNotFoundException;
import com.book_service.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Book Service - Business Logic Layer
 *
 * Layers Architecture:
 * Controller → Service → Repository → Database
 *
 * @Service: Spring manages bean lifecycle
 * @Transactional: ACID transactions
 */

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book createBook(Book book, String createdBy){
        book.setCreatedBy(createdBy);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Page<Book> getBooksWithStocks (Pageable pageable){
        return bookRepository.findByStockGreaterThanEqual(1,pageable);
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(()->new BookNotFoundException("Book not found: " + id));

    }

    public Book getBookByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found by ISBN: " + isbn));
    }

    @Transactional
    public Book updateBook(Long id, Book bookDetails,String updatedBy){
        Book existingBook = getBookById(id);

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPrice(bookDetails.getPrice());
        existingBook.setStock(bookDetails.getStock());
        existingBook.setCreatedBy(updatedBy); //Audit trail
        return bookRepository.save(existingBook);
    }

    @Transactional
    public boolean reduceStock(Long bookId, int quantity){
        Book book = getBookById(bookId);
        if(book.getStock() >= quantity){
            book.setStock(book.getStock() - quantity);
            bookRepository.save(book);
            return true ;
        }

        return false;
    }

    @Transactional
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
