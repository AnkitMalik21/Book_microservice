package com.order_service.client;


import com.order_service.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BookClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public BookDTO getBookById(Long bookId){
        return webClientBuilder.build()
                .get()
                .uri("http://book-service/books/{id}",bookId)
                .retrieve()
                .bodyToMono(BookDTO.class)
                .block();
    }
    /**
     * Calls the Book Service using WebClient to fetch a book by its ID.
     *
     * Flow:
     * 1. Builds a WebClient instance
     * 2. Sends HTTP GET request to: http://book-service/books/{id}
     * 3. Replaces {id} with provided bookId
     * 4. Converts JSON response body into BookDTO
     * 5. Blocks the thread and returns BookDTO
     * 6. Block() - wait for response . Extract value from Mono,Mono - > container for one value in future
     * Note:
     * Uses blocking call (.block()), suitable for non-reactive / simple services.
     * Retrieve() - Execute Request ,Prepare to read response
     */

    public boolean reduceStock(Long bookId,int quantity){
        try{
            webClientBuilder.build()
                    .post()
                    .uri("http://book-service/books/{id}/reduce-stock?quantity={qty}",bookId,quantity)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (Exception e) {
            System.err.println("Failed to reduce stock: " + e.getMessage());
            return false;
        }
    }

}
