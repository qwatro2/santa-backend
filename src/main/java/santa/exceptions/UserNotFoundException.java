package santa.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with ID %d not found", userId));
    }

    public UserNotFoundException(String email) {
        super(String.format("User with email %s not found", email));
    }
}
