package santa.services;

import santa.entities.Assignment;

import java.util.List;
import java.util.UUID;

public interface ShuffleService {
    List<Assignment> shuffle(UUID groupId);
}
