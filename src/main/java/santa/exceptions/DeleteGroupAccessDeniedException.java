package santa.exceptions;

import java.util.UUID;

public class DeleteGroupAccessDeniedException extends RuntimeException {
    public DeleteGroupAccessDeniedException(UUID groupId, Long userId) {
        super(String.format("Group with ID %s can't be deleted by user with id %d", groupId, userId));
    }
}
