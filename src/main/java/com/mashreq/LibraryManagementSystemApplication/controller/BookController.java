package com.mashreq.LibraryManagementSystemApplication.controller;

import com.mashreq.LibraryManagementSystemApplication.model.ApiResponse;
import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    // Add a new book
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
        log.info("Request to add a new book: {}", book);
        Book addedBook = bookService.addBook(book);
        log.info("Book added successfully: {}", addedBook);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Book added successfully", addedBook));
    }

    // Add multiple books
    @PostMapping("/add-multiple")
    public ResponseEntity<ApiResponse<List<Book>>> addMultipleBooks(@RequestBody List<Book> books) {
        log.info("Request to add multiple books: {}", books);
        List<Book> addedBooks = bookService.addMultipleBooks(books);
        log.info("Books added successfully: {}", addedBooks.size());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Books added successfully", addedBooks));
    }

    // Get all books
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        log.info("Received request to fetch all books");
        List<Book> books = bookService.getAllBooks();
        log.info("Fetched {} books successfully", books.size());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Books fetched successfully", books));
    }

    // Search for books by title, author, or genre
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        log.info("Received request to search books with title: {}, author: {}, genre: {}", title, author, genre);
        List<Book> books = bookService.searchBooks(title, author, genre);
        log.info("Found {} matching books", books.size());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Books retrieved successfully", books));
    }

//    Search for books by title, author, or genre (With Caching)
    @GetMapping("/searchBooksFromCache")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooksFromCache(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {

        log.info("Received search request with title: {}, author: {}, genre: {}", title, author, genre);
        List<Book> books = bookService.searchBooks(title, author, genre);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Books fetched successfully", books));
    }

    @GetMapping("/search-paged")
    public ResponseEntity<Page<Book>> searchBooksPaged(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Book> books = bookService.searchBooksPaged(title, author, genre, page, size);
        return ResponseEntity.ok(books);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookService.getBookById(id);
        log.info("Book retrieved: {}", book);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Book retrieved successfully", book));
    }
}
