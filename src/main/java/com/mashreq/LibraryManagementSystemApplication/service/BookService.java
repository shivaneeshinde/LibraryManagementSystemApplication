package com.mashreq.LibraryManagementSystemApplication.service;

import com.mashreq.LibraryManagementSystemApplication.exception.ResourceNotFoundException;
import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Add a single book
    public Book addBook(Book book) {
        log.info("Adding a new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    // Add multiple books using Stream API
    public List<Book> addMultipleBooks(List<Book> books) {
        log.info("Adding {} books to the repository", books.size());

        // Save all books in batch
        List<Book> savedBooks = bookRepository.saveAll(books);

        log.info("Successfully added {} books", savedBooks.size());
        return savedBooks;
    }

    // Get book by ID
    public Book getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);

        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", id);
                    return new ResourceNotFoundException("Book not found with ID: " + id);
                });
    }

    // Get all books
    public List<Book> getAllBooks() {
        log.info("Fetching all books from the database");
        return bookRepository.findAll();
    }

    // Update book status (borrowed or not)
    public void updateBookStatus(Long bookId, boolean status) {
        log.info("Updating book status for ID {}: Borrowed = {}", bookId, status);

        Book book = getBookById(bookId);
        book.setBorrowed(status);
        bookRepository.save(book);

        log.info("Book status updated successfully for ID {}", bookId);
    }

    // Search books by title, author, or genre using Stream API
    public List<Book> searchBooks(String title, String author, String genre) {
        log.info("Searching books with title: {}, author: {}, genre: {}", title, author, genre);
        return bookRepository.searchBooks(title, author, genre);
    }

    public Page<Book> searchBooksPaged(String title, String author, String genre, int page, int size) {
        log.info("Searching books with pagination: Page={}, Size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.searchBooksPaged(title, author, genre, pageable);
    }

    @Cacheable(value = "books", key = "#title + #author + #genre")
    public List<Book> searchBooksFromCache(String title, String author, String genre) {
        log.info("Fetching books from database (cache miss)");
        return bookRepository.searchBooks(title, author, genre);
    }

}
