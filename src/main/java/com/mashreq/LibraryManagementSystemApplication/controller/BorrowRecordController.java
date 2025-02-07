package com.mashreq.LibraryManagementSystemApplication.controller;

import com.mashreq.LibraryManagementSystemApplication.model.BorrowRecord;
import com.mashreq.LibraryManagementSystemApplication.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @PostMapping("/borrowBook")
    public BorrowRecord borrowBook(@RequestParam Long studentId, @RequestParam Long bookId) {
        return borrowRecordService.borrowBook(studentId, bookId);
    }

    @PostMapping("/returnBook")
    public BorrowRecord returnBook(@RequestParam Long borrowRecordId) {
        return borrowRecordService.returnBook(borrowRecordId);
    }

    @GetMapping("/searchBorrowBook")
    public List<BorrowRecord> searchBorrowRecords(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Boolean returned) {
        return borrowRecordService.searchBorrowRecords(studentId, bookId, returned);
    }

    @GetMapping("/searchBorrowBookPaged")
    public Page<BorrowRecord> searchBorrowRecordsPaged(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Boolean returned,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return borrowRecordService.searchBorrowRecordsPaged(studentId, bookId, returned, page, size);
    }

    @GetMapping("/searchBorrowBookFromCache")
    public List<BorrowRecord> searchBorrowRecordsFromCache(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Boolean returned) {
        return borrowRecordService.searchBorrowRecordsFromCache(studentId, bookId, returned);
    }

}

