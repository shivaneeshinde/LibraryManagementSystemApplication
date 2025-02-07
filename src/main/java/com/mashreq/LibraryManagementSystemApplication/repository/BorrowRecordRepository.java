package com.mashreq.LibraryManagementSystemApplication.repository;

import com.mashreq.LibraryManagementSystemApplication.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByStudentId(Long studentId);

    @Query("SELECT b FROM BorrowRecord b WHERE " +
            "(:studentId IS NULL OR b.student.id = :studentId) AND " +
            "(:bookId IS NULL OR b.book.id = :bookId) AND " +
            "(:returned IS NULL OR b.returned = :returned)")
    List<BorrowRecord> searchBorrowRecords(Long studentId, Long bookId, Boolean returned);

    @Query("SELECT b FROM BorrowRecord b WHERE " +
            "(:studentId IS NULL OR b.student.id = :studentId) AND " +
            "(:bookId IS NULL OR b.book.id = :bookId) AND " +
            "(:returned IS NULL OR b.returned = :returned)")
    Page<BorrowRecord> searchBorrowRecordsPaged(Long studentId, Long bookId, Boolean returned, Pageable pageable);

}

