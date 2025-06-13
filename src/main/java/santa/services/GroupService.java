package santa.services;

import santa.dtos.CreateGroupRequest;
import santa.dtos.JoinLinkResponse;
import santa.entities.SantaGroup;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    SantaGroup createGroup(CreateGroupRequest request, Long organizerId);

    void deleteGroup(UUID groupId, Long sourceUserId);

    void addParticipant(UUID groupId, Long userId);

    void removeParticipant(UUID participantId, Long sourceUserId);

    void addExclusion(Long sourceUserId, UUID groupId, UUID giverId, UUID receiverId);

    JoinLinkResponse getJoinLink(UUID groupId, Long sourceUserId);

    void markDone(UUID groupId, Long sourceUserId);

    List<SantaGroup> getAllOrganizedByUser(Long userId);

    List<SantaGroup> getAllParticipatedByUser(Long userId);
}
