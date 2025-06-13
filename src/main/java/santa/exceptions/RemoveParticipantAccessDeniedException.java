package santa.exceptions;

import java.util.UUID;

public class RemoveParticipantAccessDeniedException extends RuntimeException {
    public RemoveParticipantAccessDeniedException(UUID participantId, Long userId) {
        super(String.format("User with id %d can't remove participant with id = %s", userId, participantId));
    }
}
