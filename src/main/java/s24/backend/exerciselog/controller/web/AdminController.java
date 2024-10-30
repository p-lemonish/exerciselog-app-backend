package s24.backend.exerciselog.controller.web;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.domain.dto.RoleDto;
import s24.backend.exerciselog.domain.entity.Role;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.service.AdminService;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin/users";
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, Model model) throws BadRequestException {
        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/update-role/{id}")
    public String updateUserRole(@PathVariable Long id, @RequestParam String newRole, Model model) throws BadRequestException {
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleName(newRole);
        adminService.updateUserRole(id, roleDto);
        return "redirect:/admin/users";
    }

}
