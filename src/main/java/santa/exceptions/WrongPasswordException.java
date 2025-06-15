package santa.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(Long userId) {
        super(String.format("Wrong password for user with ID %d", userId));
    }
}
