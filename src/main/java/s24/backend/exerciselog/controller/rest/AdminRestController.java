package s24.backend.exerciselog.controller.rest;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import s24.backend.exerciselog.domain.dto.RoleDto;
import s24.backend.exerciselog.domain.dto.UserProfileDto;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.service.AdminService;

@RestController
public class AdminRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

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
        adminService.updateUserRole(id, roleDto);
        return ResponseEntity.status(HttpStatus.OK).body("User role updated");
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws BadRequestException {
        adminService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
