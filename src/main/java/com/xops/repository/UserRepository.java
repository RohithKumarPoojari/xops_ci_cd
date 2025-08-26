package com.xops.repository;

import com.xops.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find users by department
    List<User> findByDepartment(String department);
    
    // Find user by email
    User findByEmail(String email);
    
    // Check if user exists by email
    boolean existsByEmail(String email);
} 