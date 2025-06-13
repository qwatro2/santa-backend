package santa.exceptions;

import java.util.UUID;

public class MarkDoneAccessDeniedException extends RuntimeException {
    public MarkDoneAccessDeniedException(UUID groupId, Long userId) {
        super(String.format("User with id %d can't mark done group with ID %s", userId, groupId));
    }
}
