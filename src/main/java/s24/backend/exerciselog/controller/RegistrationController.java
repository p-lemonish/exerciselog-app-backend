package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        Model model) {
        if(!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "register";
        }
        if(userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("errorMessage", "Username is already taken");
            return "register";
        }
        if(userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage", "An account with this email is already registered");
            return "register";
        }

        Role roleUser = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Default role not found"));
        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setRole(roleUser);
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);
        return "redirect:/login";
    }
    
}
