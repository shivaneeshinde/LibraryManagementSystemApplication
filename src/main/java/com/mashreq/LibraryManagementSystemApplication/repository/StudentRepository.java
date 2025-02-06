package com.mashreq.LibraryManagementSystemApplication.repository;

import com.mashreq.LibraryManagementSystemApplication.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

