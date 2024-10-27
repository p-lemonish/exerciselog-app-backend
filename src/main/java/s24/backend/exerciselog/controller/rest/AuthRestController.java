package s24.backend.exerciselog.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.Role;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.repository.RoleRepository;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.service.CustomUserDetailsService;
import s24.backend.exerciselog.util.JwtUtil;
import s24.backend.exerciselog.util.ValidationUtil;

@RestController
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto, BindingResult result) {

        // Check validation errors in RequestBody
        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.getBody());
        }

        if(!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }
        if(userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username is already taken");
        }
        if(userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "An account with this email is already registered");
        }

        // Check other validation errors
        validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.getBody());
        }

        Role roleUser = roleRepository.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        User newUser = userMapper.toEntity(userRegistrationDto);
        newUser.setPassword(encodedPassword);
        newUser.setRole(roleUser);
        userRepository.save(newUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration OK!");
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Incorrect username or password");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    
    
}
