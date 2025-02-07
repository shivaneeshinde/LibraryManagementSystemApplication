package com.mashreq.LibraryManagementSystemApplication.service;

import com.mashreq.LibraryManagementSystemApplication.exception.StudentNotFoundException;
import com.mashreq.LibraryManagementSystemApplication.model.Student;
import com.mashreq.LibraryManagementSystemApplication.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student saveStudent(String name, String email) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
    }
}
