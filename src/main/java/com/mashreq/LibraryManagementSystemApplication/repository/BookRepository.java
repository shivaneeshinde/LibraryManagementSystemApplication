package com.mashreq.LibraryManagementSystemApplication.repository;

import com.mashreq.LibraryManagementSystemApplication.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Custom queries to search books by title, author, or genre
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenreContainingIgnoreCase(String genre);
}

