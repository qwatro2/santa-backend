package santa.dtos;

import java.util.UUID;

public record AddExclusionRequest(UUID giverId, UUID receiverId) {
}
