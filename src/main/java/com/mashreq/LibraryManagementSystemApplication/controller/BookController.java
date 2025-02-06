package com.mashreq.LibraryManagementSystemApplication.controller;

import com.mashreq.LibraryManagementSystemApplication.model.ApiResponse;
import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import org.slf4j.log;
//import org.slf4j.logFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {

    //private static final log log = logFactory.getlog(BookController.class);

    @Autowired
    private BookService bookService;

    // Add a new book
    @PostMapping("/add")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    // Add multiple books
    @PostMapping("/add-multiple")
    public ResponseEntity<String> addMultipleBooks(@RequestBody List<Book> books) {
        try {
            log.info("Request to add multiple books: {}", books);
            bookService.addMultipleBooks(books);
            log.info("Books added successfully");

            // Return success response
            return new ResponseEntity<>("Books added successfully", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred while adding books", e);
            return new ResponseEntity<>("Error adding books", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all books
    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks() {
        log.info("Received request to fetch all books");
        try {
            List<Book> books = bookService.getAllBooks();
            log.info("Fetched {} books successfully", books.size());
            return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), "Books fetched successfully", books));
        } catch (Exception e) {
            log.error("Error while fetching books: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch books", null));
        }
    }

    // Search for books by title, author, or genre
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) String genre) {
        log.info("Received request to search a book based on title");
        try {
            List<Book> books = bookService.searchBooks(title, author, genre);
            log.info("Searched {} books successfully", books.size());
            return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), "Seached Books successfully", books));
        } catch (Exception e) {
            log.error("Error while fetching books: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to search books", null));
        }
    }

    // Get book by ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

}

