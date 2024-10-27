package s24.backend.exerciselog.dto;

import jakarta.validation.constraints.*;

/*
 * String username
 * String email
 * String password
 * String confirmPassword
 */

public class UserRegistrationDto {
    @NotNull(message = "Username is required")
    @Size(min = 4, message = "Username must be at least 4 characters long")
    @Size(max = 40, message = "Username must be not be more than 40 characters long")
    private String username;

    @Email(message = "Please provide a valid email address")
    @NotNull(message = "Email is required")
    @Size(min = 4, message = "Email must be at least 4 characters long")
    @Size(max = 40, message = "Email must be not be more than 40 characters long")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Size(max = 140, message = "Password must be not be more than 140 characters long")
    private String password;

    @NotNull(message = "Confirm password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Size(max = 140, message = "Password must be not be more than 140 characters long")
    private String confirmPassword;


    public UserRegistrationDto(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserRegistrationDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
