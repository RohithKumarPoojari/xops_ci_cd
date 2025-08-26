package com.xops.service;

import com.xops.entity.User;
import com.xops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    // Create new user
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }
    
    // Update user
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setDepartment(userDetails.getDepartment());
        
        return userRepository.save(user);
    }
    
    // Delete user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
    
    // Get users by department
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }
    
    // Get user count
    public long getUserCount() {
        return userRepository.count();
    }
} 