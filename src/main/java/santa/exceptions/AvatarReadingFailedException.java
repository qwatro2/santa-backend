package santa.exceptions;

public class AvatarReadingFailedException extends RuntimeException {
    public AvatarReadingFailedException(Long userId) {
        super(String.format("Error due reading avatar of user with id = %d", userId));
    }
}
