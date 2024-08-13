package com.example.MiniBankApp.controller;

import com.example.MiniBankApp.model.User;
import com.example.MiniBankApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {

        userService.registerUser(username, password,email);
        return ResponseEntity.ok("user register successfully");
    }


        @PostMapping("/login")
        public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
            String token = userService.loginUser(username, password);
            return ResponseEntity.ok(token);
        }


}
