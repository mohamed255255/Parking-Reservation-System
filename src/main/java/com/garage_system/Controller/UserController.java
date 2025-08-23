package com.garage_system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.Model.User;
import com.garage_system.Service.UserService;


@RestController
public class UserController {
    
    private final UserService userService;
   
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/Register")
    public void registerUser(@RequestBody User user) {
          userService.RegisterUser(user);
          
    }
    
}
