package com.mashreq.LibraryManagementSystemApplication.service;

import com.mashreq.LibraryManagementSystemApplication.model.Student;
import com.mashreq.LibraryManagementSystemApplication.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
}

