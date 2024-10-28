package s24.backend.exerciselog.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import s24.backend.exerciselog.domain.Role;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.UserRegistrationDto;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.repository.RoleRepository;
import s24.backend.exerciselog.repository.UserRepository;

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
    
     public void registerUser(UserRegistrationDto userRegistrationDto) throws BadRequestException, ResourceNotFoundException {

        if(!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if(userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new BadRequestException("Username is already taken");
        }
        if(userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new BadRequestException("An account with this email is already registered");
        }

        Role roleUser = roleRepository.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        User newUser = userMapper.toEntity(userRegistrationDto);
        newUser.setPassword(encodedPassword);
        newUser.setRole(roleUser);
        userRepository.save(newUser);
    }
    
}
