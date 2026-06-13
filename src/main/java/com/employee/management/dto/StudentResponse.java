package com.employee.management.dto;

import com.employee.management.model.UserRole;

import lombok.Data;

@Data
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private UserRole role;
    private AddressDTO address;
    
}
