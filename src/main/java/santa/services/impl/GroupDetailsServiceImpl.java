package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.dtos.GroupDetailsDto;
import santa.entities.SantaGroup;
import santa.entities.User;
import santa.exceptions.GetGroupDetailsAccessDeniedException;
import santa.exceptions.GroupNotFoundException;
import santa.exceptions.UserNotFoundException;
import santa.repositories.GroupRepository;
import santa.repositories.UserRepository;
import santa.services.GroupDetailsService;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupDetailsServiceImpl implements GroupDetailsService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    public GroupDetailsDto getDetails(UUID groupId, Long sourceUserId) {
        User sourceUser = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        GroupDetailsDto result = GroupDetailsDto.fromEntity(group);

        if (Objects.equals(group.getOrganizer().getId(), sourceUser.getId())) {
            return result;
        }

        if (group.getParticipants().stream()
                .anyMatch(participant ->
                        Objects.equals(participant.getUser().getId(), sourceUser.getId()))) {
            return result;
        }

        throw new GetGroupDetailsAccessDeniedException(groupId, sourceUser.getId());
    }
}
