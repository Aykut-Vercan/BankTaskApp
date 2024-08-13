package com.example.MiniBankApp.service;

import com.example.MiniBankApp.model.User;
import com.example.MiniBankApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Kullanıcı Kaydı
    public User registerUser(String username, String password, String email) {
        // Kullanıcı adı veya e-posta ile kontrol et
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        newUser.setEmail(email);

        // Kullanıcıyı kaydet
        return userRepository.save(newUser);
    }

    // Kullanıcıyı kullanıcı adı ile bul
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Kullanıcıyı e-posta ile bul
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    // Kullanıcı Girişi

    public String loginUser(String username, String password) {

        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password,user.getPassword())) {
                return jwtService.generateToken(username);
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}