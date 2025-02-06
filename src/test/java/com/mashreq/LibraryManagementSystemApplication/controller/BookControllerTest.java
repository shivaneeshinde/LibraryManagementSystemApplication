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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
    void testAddMultipleBooksSuccess() throws Exception {
        Book book1 = new Book();
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setGenre("Java Programming");
        book1.setBorrowed(false);

        Book book2 = new Book();
        book2.setTitle("Data Structures and Algorithms in Java");
        book2.setAuthor("Robert Lafore");
        book2.setGenre("Data Structures, Algorithms");
        book2.setBorrowed(false);

        // Mock the service to return void (success) when called
        doNothing().when(bookService).addMultipleBooks(anyList());

        mockMvc.perform(post("/books/add-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(book1, book2))))
                .andExpect(status().isOk())
                .andExpect(content().string("Books added successfully"));

        // Verify that the service method was called once
        verify(bookService, times(1)).addMultipleBooks(anyList());
    }

    @Test
    void testAddMultipleBooksError() throws Exception {
        Book book1 = new Book();
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setGenre("Java Programming");
        book1.setBorrowed(false);

        // Simulate an error in the service layer
        doThrow(new RuntimeException("Database error")).when(bookService).addMultipleBooks(anyList());

        mockMvc.perform(post("/books/add-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(book1))))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error adding books"));

        // Verify the service was called
        verify(bookService, times(1)).addMultipleBooks(anyList());
    }


    @Test
    void testGetAllBooks_Success() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(
                new Book(1L, "Java 8 in Action", "Raoul-Gabriel Urma", "Technology", false),
                new Book(2L, "Data Structures and Algorithms", "Narasimha Karumanchi", "Technology", false)
        );

        when(bookService.getAllBooks()).thenReturn(mockBooks);

        // Act
        ResponseEntity<?> response = bookController.getAllBooks();
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();

        // Assert
        assertNotNull(apiResponse);
        assertEquals(200, apiResponse.getStatusCode());
        assertEquals("Books fetched successfully", apiResponse.getMessage());
        assertEquals(2, ((List<?>) apiResponse.getData()).size());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetAllBooks_Failure() {
        // Arrange
        when(bookService.getAllBooks()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = bookController.getAllBooks();
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();

        // Assert
        assertNotNull(apiResponse);
        assertEquals(500, apiResponse.getStatusCode());
        assertEquals("Failed to fetch books", apiResponse.getMessage());
        assertNull(apiResponse.getData());

        verify(bookService, times(1)).getAllBooks();
    }

}
