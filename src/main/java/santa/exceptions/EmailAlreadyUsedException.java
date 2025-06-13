package santa.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(String.format("Email %s already used", email));
    }
}
