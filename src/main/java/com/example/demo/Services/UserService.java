package com.example.demo.Services;

import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

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

    public ResponseEntity<User> createUser(User user) {

        User userToCreate = new User();

        userToCreate.setFirstName(user.getFirstName());
        userToCreate.setLastName(user.getLastName());
        userToCreate.setEmail(user.getEmail());
        userToCreate.setAge(user.getAge());

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
            userToUpdate.get().setAge(user.getAge());

            userRepository.save(userToUpdate.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new NullPointerException(String.format("Could not find user with ID: ", id));
        }


    }
}
