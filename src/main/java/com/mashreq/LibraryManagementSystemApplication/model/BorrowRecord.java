package com.mashreq.LibraryManagementSystemApplication.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Date borrowDate;
    private Date returnDate;
    private boolean returned;
}
