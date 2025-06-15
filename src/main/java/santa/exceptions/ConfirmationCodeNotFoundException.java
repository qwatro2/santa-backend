package santa.exceptions;

public class ConfirmationCodeNotFoundException extends RuntimeException {
    public ConfirmationCodeNotFoundException(Long userId) {
        super(String.format("Confirmation code for user with ID %d not found", userId));
    }
}
