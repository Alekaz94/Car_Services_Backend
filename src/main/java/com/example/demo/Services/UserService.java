package com.example.demo.Services;

import com.example.demo.Authentication.PasswordEncoding;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entities.User;
import com.example.demo.Enums.Roles;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    PasswordEncoding pe = new PasswordEncoding();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(UUID id) {
        if(id == null) {
            throw new NullPointerException("No id was entered.");
        }

        Optional<User> foundUser = userRepository.findById(id);

        if(foundUser.isPresent()) {
            return foundUser.get();
        } else {
            throw new NullPointerException(String.format("Could not find user with ID: ", id));
        }
    }

    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {

        User userToCreate = new User();
        String passwordHashed = pe.hashPassword(userDTO.getPassword());

        userToCreate.setFirstName(userDTO.getFirstName());
        userToCreate.setLastName(userDTO.getLastName());
        userToCreate.setEmail(userDTO.getEmail());
        userToCreate.setPassword(passwordHashed);
        userToCreate.setRole(Roles.EMPLOYEE);

        userRepository.save(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<User> updateUser(UUID id, User user) {
        if(id == null) {
            throw new NullPointerException(String.format("No user with ID: ", id));
        }

        if(user == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<User> userToUpdate = userRepository.findById(id);

        if(userToUpdate.isPresent()) {
            userToUpdate.get().setFirstName(user.getFirstName());
            userToUpdate.get().setLastName(user.getLastName());

            userRepository.save(userToUpdate.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new NullPointerException(String.format("Could not find user with ID: ", id));
        }
    }

    public ResponseEntity<User> deleteUser(UUID id) {
        if(id == null) {
            throw new NullPointerException(String.format("No user with ID: ", id));
        }

        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new NullPointerException(String.format("Could not find user with ID: ", id));
        }
    }

    public User login(LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if(loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password must not be empty");
        }

        Optional<User> checkIfUserExists = userRepository.findByEmail(loginDTO.getEmail());
        if (checkIfUserExists.isEmpty()) {
            throw new IllegalArgumentException("No user with that email found!");
        }

        User foundUser = checkIfUserExists.get();

        boolean checkPasswordForMatch = pe.checkHashedPassword(loginDTO.getPassword(), foundUser.getPassword());
        if(!checkPasswordForMatch) {
            throw new IllegalArgumentException("Invalid password!");
        }

        return foundUser;
    }

    public User signUp(UserDTO userDTO) {
        Optional<User> checkIfUserExists = userRepository.findByEmail(userDTO.getEmail());
        if (checkIfUserExists.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        if(userDTO.getPassword() == null || userDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password must not be empty");
        }

        User user = new User();
        String password = pe.hashPassword(userDTO.getPassword());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(password);
        user.setRole(Roles.CUSTOMER);

        return userRepository.save(user);
    }
}
