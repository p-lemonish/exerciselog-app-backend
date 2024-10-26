package s24.backend.exerciselog.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/home";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
