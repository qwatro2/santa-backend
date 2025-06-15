package santa.exceptions;

public class WrongConfirmationCodeException extends RuntimeException {
    public WrongConfirmationCodeException(Long userId, String code) {
        super(String.format("Wrong code %s for user with ID %d", code, userId));
    }
}
