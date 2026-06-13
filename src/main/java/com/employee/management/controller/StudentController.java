package com.employee.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.service.StudentService;
import com.employee.management.dto.StudentRequest;
import com.employee.management.dto.StudentResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity <List<StudentResponse>> getStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping("/students")
    public ResponseEntity <String> addStudent(@RequestBody StudentRequest studentRequest){ 
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(studentRequest));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity <StudentResponse> getStudent(@PathVariable Long id){
        StudentResponse response = studentService.getStudentById(id);
        if(response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity <StudentResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRequest student){
        StudentResponse res = studentService.updateStudent(id, student);
        if(res == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }
    
    @DeleteMapping("/students/{id}")
    public ResponseEntity <String> deleteStudent(@PathVariable Long id){
        String res = studentService.deleteStudent(id);
        if(res.equals("Student Not Found")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
 