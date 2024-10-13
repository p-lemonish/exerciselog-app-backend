package s24.backend.exerciselog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.repository.UserRepository;

@Component
public class SecurityUtils {
    private static UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated.");
        }
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return currentUser;
    }
}
