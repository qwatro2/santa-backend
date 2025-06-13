package santa.exceptions;

import java.util.UUID;

public class GetJoinLinkAccessDeniedException extends RuntimeException {
    public GetJoinLinkAccessDeniedException(UUID groupId, Long userId) {
        super(String.format("User with id %d can't get join link for group with ID %s", userId, groupId));
    }
}
