package com.example.demo;

import com.example.demo.Entities.User;
import com.example.demo.Enums.Roles;
import com.example.demo.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CarServiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarServiceBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByEmail("admin.admin@work.com").isEmpty()) {
				User adminUser = new User();
				adminUser.setFirstName("Admin");
				adminUser.setLastName("Admin");
				adminUser.setEmail("admin.admin@work.com");
				adminUser.setPassword(passwordEncoder.encode("Admin123"));
				adminUser.setRole(Roles.ADMIN);

				userRepository.save(adminUser);
			}
		};
	}
}
