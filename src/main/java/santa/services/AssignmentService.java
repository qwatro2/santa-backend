package santa.services;

import santa.entities.Assignment;

import java.util.UUID;

public interface AssignmentService {
    Assignment findByGroupAndSantaUser(UUID groupId, Long userId);
}
