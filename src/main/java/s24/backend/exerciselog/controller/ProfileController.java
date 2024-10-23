package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.mapper.*;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;


@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        User user = SecurityUtils.getCurrentUser();
        UserProfileDto userProfileDto = userMapper.toProfileDto(user);
        model.addAttribute("user", userProfileDto);

        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        model.addAttribute("passwordChangeDto", passwordChangeDto);
        return "profile";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute PasswordChangeDto passwordChangeDto, 
        BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User user = SecurityUtils.getCurrentUser();
        UserProfileDto userProfileDto = userMapper.toProfileDto(user);

        // Catch validation errors
        if(result.hasErrors()) {
            model.addAttribute("user", userProfileDto);
            return "profile";
        }

        // Catch errors for current password
        if(!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
            result.rejectValue("currentPassword", "error.passwordChangeDto.currentPassword", "Current password is wrong");
            model.addAttribute("user", userProfileDto);
            return "profile";
        }

        // Catch new password and confirm password not matching
        if(!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            result.rejectValue("confirmNewPassword", "error.passwordChangeDto.confirmNewPassword", "New password and confirm new password don't match");
            model.addAttribute("user", userProfileDto);
            return "profile";
        }

        String encodedNewPassword = passwordEncoder.encode(passwordChangeDto.getConfirmNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        // model.addAttributes wouldn't work with redirect:/profile
        redirectAttributes.addFlashAttribute("message", "Password changed succesfully");
        return "redirect:/profile";
    }
    
    
}
