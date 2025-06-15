package santa.exceptions;

import java.util.UUID;

public class DeleteExclusionAccessDeniedException extends RuntimeException {
    public DeleteExclusionAccessDeniedException(UUID exclusionId, UUID groupId, Long userId) {
        super(String.format("Exclusion with ID %s in group with ID %s can't be deleted by user with id %d",
                exclusionId, groupId, userId));
    }
}
