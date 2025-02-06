package com.mashreq.LibraryManagementSystemApplication.service;

import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
//import org.slf4j.log;
//import org.slf4j.logFactory;

@Service
@Slf4j
public class BookService {

    //private static final log log = logFactory.getlog(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    // Add multiple books using Stream
    public void addMultipleBooks(List<Book> books) {
        try {
            log.info("Adding multiple books using stream");

            // Process each book and save it to the repository
            List<Book> processedBooks = books.stream()
                    .map(book -> {
                        log.debug("Processing book: {}", book.getTitle());
                        return book;
                    })
                    .collect(Collectors.toList());

            // Save all books in one batch
            bookRepository.saveAll(processedBooks);
            log.info("All books have been added successfully");
        } catch (Exception e) {
            log.error("Error occurred while adding books", e);
            throw e;
        }
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Get all books
    public List<Book> getAllBooks() {
        log.info("Fetching all books from the database");
        return bookRepository.findAll();
    }

    public void updateBookStatus(Long bookId, boolean status) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setBorrowed(status);
            addBook(book);
        }
    }

    // Search books by title, author, or genre using Stream
    public List<Book> searchBooks(String title, String author, String genre) {
        List<Book> books = bookRepository.findAll(); // Fetch all books from the DB

        // Use Java Streams to filter based on provided parameters
        return books.stream()
                .filter(book -> (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(book -> (author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase())))
                .filter(book -> (genre == null || book.getGenre().toLowerCase().contains(genre.toLowerCase())))
                .collect(Collectors.toList());
    }
}

