package com.book_service.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String message){
        super(message);
    }

    public BookNotFoundException(String field, String value) {
        super("Book not found with " + field + ": " + value);
    }

}
