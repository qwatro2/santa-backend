package santa.exceptions;

import java.util.UUID;

public class GetExclusionAccessDeniedException extends RuntimeException {
    public GetExclusionAccessDeniedException(UUID exclusionId, UUID groupId, Long userId) {
        super(String.format("Exclusion with ID %s in group with ID %s can't be got by user with id %d",
                exclusionId, groupId, userId));
    }
}
