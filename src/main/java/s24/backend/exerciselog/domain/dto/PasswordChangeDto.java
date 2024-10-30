package s24.backend.exerciselog.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PasswordChangeDto {

    @NotEmpty(message = "Current password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Size(max = 140, message = "Password must be not be more than 140 characters long")
    private String currentPassword;

    @NotEmpty(message = "New password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Size(max = 140, message = "Password must be not be more than 140 characters long")
    private String newPassword;

    @NotEmpty(message = "confirm password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Size(max = 140, message = "Password must be not be more than 140 characters long")
    private String confirmNewPassword;

    public PasswordChangeDto() {}
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

}
