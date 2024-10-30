package s24.backend.exerciselog.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import s24.backend.exerciselog.domain.dto.PasswordChangeDto;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.util.SecurityUtils;

@Service
public class ProfileService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    
    public void changeUserPassword(PasswordChangeDto passwordChangeDto) throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();

        // Catch errors for current password
        if(!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is wrong");
        }

        // Catch new password and confirm password not matching
        if(!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            throw new BadRequestException("New password and confirm new password don't match");
        }
        
        String encodedNewPassword = passwordEncoder.encode(passwordChangeDto.getConfirmNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}
