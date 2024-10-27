package s24.backend.exerciselog.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.PasswordChangeDto;
import s24.backend.exerciselog.dto.UserProfileDto;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.util.SecurityUtils;
import s24.backend.exerciselog.util.ValidationUtil;

@RestController
public class ProfileRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/profile")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        User user = SecurityUtils.getCurrentUser();
        UserProfileDto userProfileDto = userMapper.toProfileDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
    }

    @PostMapping("/api/profile/change-password")
    public ResponseEntity<?> changeUserPassword(
        @Valid @RequestBody PasswordChangeDto passwordChangeDto, BindingResult result) {
        
        User user = SecurityUtils.getCurrentUser();

        // Check validation errors in RequestBody
        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.getBody());
        }
        
        // Catch errors for current password
        if(!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
            result.rejectValue("currentPassword", "error.passwordChangeDto.currentPassword", "Current password is wrong");
        }

        // Catch new password and confirm password not matching
        if(!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            result.rejectValue("confirmNewPassword", "error.passwordChangeDto.confirmNewPassword", "New password and confirm new password don't match");
        }
        
        //Check for new validation errors
        validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.getBody());
        }

        String encodedNewPassword = passwordEncoder.encode(passwordChangeDto.getConfirmNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully.");
    }
    
    
}
