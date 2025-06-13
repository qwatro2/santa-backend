package santa.exceptions;

import java.util.UUID;

public class ParticipantAlreadyExistsException extends RuntimeException {
    public ParticipantAlreadyExistsException(UUID groupId, Long userId) {
        super(String.format("User with ID %d already in Group with ID %s", userId, groupId));
    }
}
