package com.employee.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.employee.management.dto.AddressDTO;
import com.employee.management.dto.StudentRequest;
import com.employee.management.dto.StudentResponse;
import com.employee.management.model.Address;
import com.employee.management.model.Student;
import com.employee.management.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class StudentService {
    
    private final StudentRepository studentRepository;

    public List <StudentResponse> getAllStudents(){
        return studentRepository.findAll().stream()
        .map(this::mapToUserResponse)
        .collect(Collectors.toList());
    } 
    public String addStudent(Student student){
        studentRepository.save(student);
        return "Student added Successfully";
    }
    public StudentResponse getStudentById(Long id){
        return studentRepository.findById(id).map(this::mapToUserResponse).orElse(null);
    }
    public StudentResponse updateStudent(Long id, StudentRequest s){
        Student student = studentRepository.findById(id).orElse(null);
        if(student == null){
            return null;
        }
        student.setFirstName(s.getFirstName());
        student.setLastName(s.getLastName());
        student.setEmail(s.getEmail());
        student.setPhoneNumber(s.getPhoneNumber());
        student.setUserRole(s.getRole());
        
        if(s.getAddress() != null){
            Address address = student.getAddress();
            if(address == null){
                address = new Address();
            }
            address.setStreet(s.getAddress().getStreet());
            address.setCity(s.getAddress().getCity());
            address.setState(s.getAddress().getState());
            address.setZipCode(s.getAddress().getZipCode());
            address.setCountry(s.getAddress().getCountry());
        }
        Student updatedStudent = studentRepository.save(student);
        return mapToUserResponse(updatedStudent);
        
    }
    

    public String deleteStudent(Long id){
        return studentRepository.findById(id).map(student -> {
            studentRepository.delete(student);
            return "Student Removed Successfully";
        }).orElse("Student Not Found");
    }
    private StudentResponse mapToUserResponse(Student student){
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setPhoneNumber(student.getPhoneNumber());
        response.setEmail(student.getEmail());
        response.setRole(student.getUserRole());
        if(student.getAddress() != null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(student.getAddress().getStreet());
            addressDTO.setCity(student.getAddress().getCity());
            addressDTO.setState(student.getAddress().getState());
            addressDTO.setZipCode(student.getAddress().getZipCode());
            response.setAddress(addressDTO);
        }
        return response; 
    }
}
