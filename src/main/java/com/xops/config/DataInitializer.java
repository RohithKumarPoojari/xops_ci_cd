package com.xops.config;

import com.xops.entity.User;
import com.xops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        userRepository.deleteAll();
        
        // Create sample users
        User user1 = new User("John Doe", "john.doe@xops.com", "Engineering");
        User user2 = new User("Jane Smith", "jane.smith@xops.com", "Marketing");
        User user3 = new User("Bob Johnson", "bob.johnson@xops.com", "Engineering");
        User user4 = new User("Alice Brown", "alice.brown@xops.com", "HR");
        
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        
        System.out.println("Sample data initialized successfully!");
    }
} 