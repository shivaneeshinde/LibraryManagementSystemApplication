package com.mashreq.LibraryManagementSystemApplication.controller;

import com.mashreq.LibraryManagementSystemApplication.model.BorrowRecord;
import com.mashreq.LibraryManagementSystemApplication.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

