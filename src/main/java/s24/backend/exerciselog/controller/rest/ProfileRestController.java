package s24.backend.exerciselog.controller.rest;

import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileRestController {

    @GetMapping("/api/profile")
    public String getUserProfile(@RequestParam String param) {
        // TODO
        return new String();
    }

    @PostMapping("/api/profile/change-password")
    public String changeUserPassword(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
}
