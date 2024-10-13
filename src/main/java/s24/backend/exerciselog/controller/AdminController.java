package s24.backend.exerciselog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin/users";
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        User currentUser = SecurityUtils.getCurrentUser();
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userToDelete.getUsername().equals(currentUser.getUsername())) {
            model.addAttribute("errorMessage", "You cannot delete your own account.");
            return getAllUsers(model);
        }

        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/update-role/{id}")
    public String updateUserRole(@PathVariable Long id, @RequestParam String newRole, Model model) {
        User currentUser = SecurityUtils.getCurrentUser();

        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Role roleToSet = roleRepository.findByName(newRole).orElseThrow(() -> new RuntimeException("Role not found"));

        if (userToUpdate.getUsername().equals(currentUser.getUsername())) {
            model.addAttribute("errorMessage", "You cannot change your own role.");
            return getAllUsers(model);
        }


        userToUpdate.setRole(roleToSet);
        userRepository.save(userToUpdate);
        return "redirect:/admin/users";
    }

}
