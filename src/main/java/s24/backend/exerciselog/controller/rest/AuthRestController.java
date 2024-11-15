package s24.backend.exerciselog.controller.rest;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.*;

import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.service.CustomUserDetailsService;
import s24.backend.exerciselog.service.RegistrationService;
import s24.backend.exerciselog.util.JwtUtil;
import s24.backend.exerciselog.util.SecurityUtils;
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

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto, BindingResult result) throws BadRequestException, ResourceNotFoundException, GeneralSecurityException {

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
        String username = authRequest.getUsername();
        String usernameHash = SecurityUtils.hash(username);
        String password = authRequest.getPassword();

        final User user = userRepository.findByUsernameHash(usernameHash).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password));
        } catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Incorrect username or password");
        }
        final Long userId = user.getId();
        final String jwt = jwtUtil.generateToken(userId);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    
    
}
