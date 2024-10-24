package s24.backend.exerciselog.controller.rest;

import org.springframework.web.bind.annotation.*;

@RestController
public class AdminRestController {
    
    @GetMapping("/api/admin/users")
    public String getAllUsers(@RequestParam String param) {
        //TODO
        return new String();
    }

    @PutMapping("/api/admin/users/{id}")
    public String editUserRole(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }

    @DeleteMapping("/api/admin/users/{id}")
    public String deleteUser(@PathVariable String id) {
        //TODO
        return null;
    }
    
}
