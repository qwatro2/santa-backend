package santa.exceptions;

import java.util.UUID;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(UUID groupId) {
        super(String.format("Group with ID %s not found", groupId));
    }
}
