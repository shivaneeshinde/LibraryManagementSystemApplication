package com.mashreq.LibraryManagementSystemApplication.service;

import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.model.BorrowRecord;
import com.mashreq.LibraryManagementSystemApplication.model.Student;
import com.mashreq.LibraryManagementSystemApplication.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
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

    public List<BorrowRecord> searchBorrowRecords(Long studentId, Long bookId, Boolean returned) {
        log.info("Searching borrow records with studentId: {}, bookId: {}, returned: {}", studentId, bookId, returned);
        return borrowRecordRepository.searchBorrowRecords(studentId, bookId, returned);
    }

    public Page<BorrowRecord> searchBorrowRecordsPaged(Long studentId, Long bookId, Boolean returned, int page, int size) {
        log.info("Searching borrow records with pagination: Page={}, Size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return borrowRecordRepository.searchBorrowRecordsPaged(studentId, bookId, returned, pageable);
    }

    @Cacheable(value = "borrowRecords", key = "#studentId + #bookId + #returned")
    public List<BorrowRecord> searchBorrowRecordsFromCache(Long studentId, Long bookId, Boolean returned) {
        log.info("Fetching borrow records from database (cache miss)");
        return borrowRecordRepository.searchBorrowRecords(studentId, bookId, returned);
    }

}

