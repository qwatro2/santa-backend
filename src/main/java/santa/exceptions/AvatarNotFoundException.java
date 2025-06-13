package santa.exceptions;

public class AvatarNotFoundException extends RuntimeException {
    public AvatarNotFoundException(Long userId) {
        super(String.format("Avatar of user with ID %d not found", userId));
    }
}
