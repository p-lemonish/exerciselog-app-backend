package s24.backend.exerciselog.controller.rest;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.dto.PasswordChangeDto;
import s24.backend.exerciselog.domain.dto.UserProfileDto;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.mapper.UserMapper;
import s24.backend.exerciselog.service.ProfileService;
import s24.backend.exerciselog.util.SecurityUtils;
import s24.backend.exerciselog.util.ValidationUtil;

@RestController
public class ProfileRestController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProfileService profileService;

    @GetMapping("/api/profile")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        User user = SecurityUtils.getCurrentUser();
        UserProfileDto userProfileDto = userMapper.toProfileDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
    }

    @PostMapping("/api/profile/change-password")
    public ResponseEntity<?> changeUserPassword(
        @Valid @RequestBody PasswordChangeDto passwordChangeDto, BindingResult result) throws BadRequestException {
        
        // Check validation errors in RequestBody
        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.getBody());
        }

        profileService.changeUserPassword(passwordChangeDto);

        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully.");
    }
}
