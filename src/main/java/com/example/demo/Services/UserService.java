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
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User createUser(UserRequest userRequest) {
        User userToCreate = new User();
        String workEmail = (userRequest.firstName() + "." + userRequest.lastName() + "@work.com");
        String passwordHashed = passwordEncoding.hashPassword(userRequest.password());

        userToCreate.setFirstName(userRequest.firstName());
        userToCreate.setLastName(userRequest.lastName());
        userToCreate.setEmail(workEmail);
        userToCreate.setPassword(passwordHashed);
        userToCreate.setRole(Roles.EMPLOYEE);

        userRepository.save(userToCreate);
        return userToCreate;
    }

    public User updateUser(UUID id, User user) {
        if(id == null) {
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
        }

        if(user == null) {
            throw new NullPointerException("No id parameter given!");
        }

        User userToUpdate = getUserById(id);

        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userRepository.save(userToUpdate);
        return userToUpdate;

    }

    public User deleteUser(UUID id) {
        if(id == null) {
            throw new NullPointerException(String.format("Could not find user with ID: %s", id));
        }

        User foundUser = getUserById(id);
        userRepository.deleteById(id);
        return foundUser;
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
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered!");
        }

        User user = new User();

        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoding.hashPassword(userRequest.password()));
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

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
