package s24.backend.exerciselog.controller.rest;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.service.CustomUserDetailsService;
import s24.backend.exerciselog.service.RegistrationService;
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
    private RegistrationService registrationService;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto, BindingResult result) throws BadRequestException, ResourceNotFoundException {

        // Check validation errors in RequestBody
        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.getBody());
        }

        registrationService.registerUser(userRegistrationDto);
        
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
