package com.mashreq.LibraryManagementSystemApplication.controller;

import com.mashreq.LibraryManagementSystemApplication.model.ApiResponse;
import com.mashreq.LibraryManagementSystemApplication.model.Book;
import com.mashreq.LibraryManagementSystemApplication.model.Student;
import com.mashreq.LibraryManagementSystemApplication.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Student>> addStudent(@RequestBody Student student) {
        log.info("Request to add a new student: {}", student);
        Student addedStudent = studentService.addStudent(student);
        log.info("Book added successfully: {}", addedStudent);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Student added successfully", addedStudent));

    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
}
