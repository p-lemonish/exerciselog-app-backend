package s24.backend.exerciselog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.util.SecurityUtils;

//Fix for role not passing correctly to SecurityConfig
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String usernameHash = SecurityUtils.hash(username);
        User user = userRepository.findByUsernameHash(usernameHash).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String role = "ROLE_" + user.getRole().getName(); //Spring security wants roles as ROLE_rolename
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.createAuthorityList(role));
    }
}