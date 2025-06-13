package santa.exceptions;

public class AvatarSavingFailedException extends RuntimeException {
    public AvatarSavingFailedException(Long userId) {
        super(String.format("Error due saving avatar of user with id = %d", userId));
    }
}
