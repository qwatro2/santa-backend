package santa.exceptions;

import java.util.UUID;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(UUID groupId, Long userId) {
        super(String.format("Assignment in group with ID %s for user with id %d not found", groupId, userId));
    }
}
