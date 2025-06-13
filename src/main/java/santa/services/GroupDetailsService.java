package santa.services;

import santa.dtos.GroupDetailsDto;

import java.util.UUID;

public interface GroupDetailsService {
    GroupDetailsDto getDetails(UUID groupId, Long sourceUserId);
}
