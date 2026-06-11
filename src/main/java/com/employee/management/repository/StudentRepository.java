package com.employee.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employee.management.model.Student;

@Repository
public interface StudentRepository extends JpaRepository <Student, Long> {
    
}
    

