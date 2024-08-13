package com.example.MiniBankApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MiniBankAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankAppApplication.class, args);
		System.out.println("-------***********-------------");
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "dali";
		String encodedPassword = encoder.encode(rawPassword);

		boolean isMatch = encoder.matches(rawPassword, encodedPassword);
		System.out.println("Password match: " + isMatch);
	}

}
