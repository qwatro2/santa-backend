package santa.exceptions;

import java.util.UUID;

public class ExclusionAlreadyExistsException extends RuntimeException {
    public ExclusionAlreadyExistsException(UUID giverId, UUID receiverId) {
        super(String.format("Exclusion with Giver with ID %s " +
                "and Receiver with ID %s already exists", giverId, receiverId));
    }
}
