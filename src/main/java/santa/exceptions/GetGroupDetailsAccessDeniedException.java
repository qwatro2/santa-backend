package santa.exceptions;

import java.util.UUID;

public class GetGroupDetailsAccessDeniedException extends RuntimeException {
    public GetGroupDetailsAccessDeniedException(UUID groupId, Long sourceUserId) {
        super(String.format("User with id = %d can't get details for group with id = %s", sourceUserId, groupId));
    }
}
