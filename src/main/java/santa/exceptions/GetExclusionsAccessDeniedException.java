package santa.exceptions;

import java.util.UUID;

public class GetExclusionsAccessDeniedException extends RuntimeException {
    public GetExclusionsAccessDeniedException(UUID groupId, Long userId) {
        super(String.format("User with id %d can't get exclusions in group with ID %s", userId, groupId));
    }
}
