package com.mashreq.LibraryManagementSystemApplication.repository;

import com.mashreq.LibraryManagementSystemApplication.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByStudentId(Long studentId);
}

