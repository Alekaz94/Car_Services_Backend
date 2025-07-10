package com.example.demo.Services;

import com.example.demo.Authentication.JwtUtil;
import com.example.demo.Authentication.PasswordEncoding;
import com.example.demo.DTO.JwtResponse;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.UserRequest;
import com.example.demo.Entities.User;
import com.example.demo.Enums.Roles;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoding passwordEncoding;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoding passwordEncoding) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoding = passwordEncoding;
    }

    @Autowired
    public void setAuthenticationManager(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
        }
    }

    public ResponseEntity<UserRequest> createUser(UserRequest userRequest) {

        User userToCreate = new User();
        String workEmail = (userRequest.getFirstName() + "." + userRequest.getLastName() + "@work.com");
        String passwordHashed = passwordEncoding.hashPassword(userRequest.getPassword());

        userToCreate.setFirstName(userRequest.getFirstName());
        userToCreate.setLastName(userRequest.getLastName());
        userToCreate.setEmail(workEmail);
        userToCreate.setPassword(passwordHashed);
        userToCreate.setRole(Roles.EMPLOYEE);

        userRepository.save(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<User> updateUser(UUID id, User user) {
        if(id == null) {
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
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
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
        }
    }

    public ResponseEntity<User> deleteUser(UUID id) {
        if(id == null) {
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
        }

        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
        }
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No user found!"));

            return ResponseEntity.ok(new JwtResponse(user, token));
        } catch(AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
        }
    }

    public User signUp(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered!");
        }

        User user = new User();

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoding.hashPassword(userRequest.getPassword()));
        user.setRole(Roles.CUSTOMER);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().toString()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
