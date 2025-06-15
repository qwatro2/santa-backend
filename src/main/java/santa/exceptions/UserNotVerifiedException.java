package santa.exceptions;

public class UserNotVerifiedException extends RuntimeException {
    public UserNotVerifiedException(Long userId) {
        super(String.format("User with ID %d not verified", userId));
    }

}
