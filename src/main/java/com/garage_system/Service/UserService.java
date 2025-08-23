package com.garage_system.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage_system.Model.User;
import com.garage_system.Repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
     
     @Autowired
     public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

     public void RegisterUser(User user) {
          userRepository.save(user);
     }

}
