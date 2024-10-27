package s24.backend.exerciselog.controller.rest;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import s24.backend.exerciselog.domain.Role;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.RoleDto;
import s24.backend.exerciselog.dto.UserProfileDto;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.repository.RoleRepository;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.util.SecurityUtils;

@RestController
public class AdminRestController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;
    
    @GetMapping("/api/admin/users")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserProfileDto> userProfileDtos = userMapper.toProfileDtoList(users);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileDtos);
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserProfileDto userProfileDto = userMapper.toProfileDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<?> editUserRole(@PathVariable Long id, @RequestBody RoleDto roleDto) throws ResourceNotFoundException, BadRequestException {
        String roleName = roleDto.getRoleName();
        if(roleName == null || roleName == "") {
            throw new BadRequestException("Role name must not be empty");
        }
        User currentUser = SecurityUtils.getCurrentUser();
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role roleToSet = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (userToUpdate.getUsername().equals(currentUser.getUsername())) {
            throw new BadRequestException("You cannot change your own role");
        }

        userToUpdate.setRole(roleToSet);
        userRepository.save(userToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("User role updated");
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws BadRequestException {
        User currentUser = SecurityUtils.getCurrentUser();
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userToDelete.getUsername().equals(currentUser.getUsername())) {
            throw new BadRequestException("Cannot delete your own account");
        }

        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
