package s24.backend.exerciselog.service;

import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import s24.backend.exerciselog.domain.dto.UserRegistrationDto;
import s24.backend.exerciselog.domain.entity.Role;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.exception.BadRequestException;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.repository.RoleRepository;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.util.SecurityUtils;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void registerUser(UserRegistrationDto userRegistrationDto) throws BadRequestException, ResourceNotFoundException, GeneralSecurityException {
        String username = userRegistrationDto.getUsername().toLowerCase();
        userRegistrationDto.setUsername(username);
        String usernameHash = SecurityUtils.hash(username);
        String email = userRegistrationDto.getEmail().toLowerCase();
        userRegistrationDto.setEmail(email);
        String emailHash = SecurityUtils.hash(email);

        if(!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if(userRepository.findByUsernameHash(usernameHash).isPresent()) {
            throw new BadRequestException("Username is already taken");
        }
        if(userRepository.findByEmailHash(emailHash).isPresent()) {
            throw new BadRequestException("An account with this email is already registered");
        }

        Role roleUser = roleRepository.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        User newUser = userMapper.toEntity(userRegistrationDto);
        newUser.setEmailHash(emailHash);
        newUser.setUsernameHash(usernameHash);
        newUser.setPassword(encodedPassword);
        newUser.setRole(roleUser);
        userRepository.save(newUser);
    }
    
}
