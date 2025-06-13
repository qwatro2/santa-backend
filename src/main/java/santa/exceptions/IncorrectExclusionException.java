package santa.exceptions;

import java.util.UUID;

public class IncorrectExclusionException extends RuntimeException {
    public IncorrectExclusionException(Long sourceUserId, UUID giverId, UUID receiverId) {
        super(String.format("User with ID %d can't add exclusion " +
                "with Giver with ID %s and Receiver with ID %s", sourceUserId, giverId, receiverId));
    }
}
