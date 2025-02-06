package com.mashreq.LibraryManagementSystemApplication.service;

import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.model.BorrowRecord;
import com.mashreq.LibraryManagementSystemApplication.model.Student;
import com.mashreq.LibraryManagementSystemApplication.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BorrowRecordService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private StudentService studentService;

    public BorrowRecord borrowBook(Long studentId, Long bookId) {
        Book book = bookService.getBookById(bookId);
        if (book != null && !book.isBorrowed()) {
            Student student = studentService.getStudentById(studentId);
            if (student != null) {
                BorrowRecord record = new BorrowRecord();
                record.setStudent(student);
                record.setBook(book);
                record.setBorrowDate(new Date());
                record.setStatus("borrowed");

                // Update book status
                bookService.updateBookStatus(bookId, true);

                return borrowRecordRepository.save(record);
            }
        }
        return null;
    }

    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId).orElse(null);
        if (record != null && "borrowed".equals(record.getStatus())) {
            record.setReturnDate(new Date());
            record.setStatus("returned");

            // Update book status
            bookService.updateBookStatus(record.getBook().getId(), false);

            return borrowRecordRepository.save(record);
        }
        return null;
    }
}

