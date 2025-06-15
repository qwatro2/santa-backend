package santa.dtos;

public record ChangePasswordRequest(String oldPassword, String newPassword) {
}
