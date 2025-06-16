package santa.exceptions;

import java.util.UUID;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(UUID userId) {
        super(String.format("Participant with ID %s not found", userId));
    }

    public ParticipantNotFoundException(UUID groupId, Long userId) {
        super(String.format("Participant with user ID %d in group with ID %s not found", userId, groupId));
    }
}
