package com.mashreq.LibraryManagementSystemApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashreq.LibraryManagementSystemApplication.model.ApiResponse;
import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddBookSuccess() throws Exception {
        Book book = new Book(1L, "Java Concurrency in Practice", "Brian Goetz", "Programming", false);
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book added successfully"));

        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void testAddMultipleBooksSuccess() throws Exception {
        Book book1 = new Book(1L, "Effective Java", "Joshua Bloch", "Java", false);
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "Programming", false);
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.addMultipleBooks(anyList())).thenReturn(books);

        mockMvc.perform(post("/books/add-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(books)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Books added successfully"));

        verify(bookService, times(1)).addMultipleBooks(anyList());
    }

    @Test
    void testGetAllBooksSuccess() {
        List<Book> books = Arrays.asList(
                new Book(1L, "Refactoring", "Martin Fowler", "Software Engineering", false),
                new Book(2L, "Design Patterns", "Erich Gamma", "Software Design", false)
        );
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<ApiResponse<List<Book>>> response = bookController.getAllBooks();

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(2, response.getBody().getData().size());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testSearchBooksSuccess() throws Exception {
        List<Book> books = Arrays.asList(new Book(1L, "Head First Java", "Kathy Sierra", "Java", false));
        when(bookService.searchBooks("Head First Java", null, null)).thenReturn(books);

        mockMvc.perform(get("/books/search")
                        .param("title", "Head First Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());

        verify(bookService, times(1)).searchBooks("Head First Java", null, null);
    }

    @Test
    void testGetBookByIdSuccess() throws Exception {
        Book book = new Book(1L, "You Don't Know JS", "Kyle Simpson", "JavaScript", false);
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book retrieved successfully"))
                .andExpect(jsonPath("$.data.title").value("You Don't Know JS"));

        verify(bookService, times(1)).getBookById(1L);
    }
}
