package santa.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.dtos.CreateGroupRequest;
import santa.dtos.ExclusionDto;
import santa.dtos.JoinLinkResponse;
import santa.dtos.ListExclusionDto;
import santa.entities.Exclusion;
import santa.entities.Participant;
import santa.entities.SantaGroup;
import santa.entities.User;
import santa.exceptions.*;
import santa.repositories.ExclusionRepository;
import santa.repositories.GroupRepository;
import santa.repositories.ParticipantRepository;
import santa.repositories.UserRepository;
import santa.services.GroupService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ExclusionRepository exclusionRepository;

    @Transactional
    @Override
    public SantaGroup createGroup(CreateGroupRequest request, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));

        SantaGroup group = new SantaGroup();
        group.setName(request.name());
        group.setExchangeDate(request.exchangeDate());
        group.setCostMin(request.costMin());
        group.setCostMax(request.costMax());
        group.setOrganizer(organizer);
        group.setShuffled(false);

        return groupRepository.save(group);
    }

    @Transactional
    @Override
    public void deleteGroup(UUID groupId, Long sourceUserId) {
        User sourceUser = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        if (Objects.equals(group.getOrganizer().getId(), sourceUser.getId())) {
            throw new DeleteGroupAccessDeniedException(groupId, sourceUserId);
        }
        groupRepository.delete(group);
    }

    @Transactional
    @Override
    public void addParticipant(UUID groupId, Long userId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (participantRepository.existsByGroupAndUser(group, user)) {
            throw new ParticipantAlreadyExistsException(groupId, userId);
        }

        Participant participant = new Participant();
        participant.setUser(user);
        participant.setGroup(group);
        participant.setActive(true);

        participantRepository.save(participant);
    }

    @Transactional
    @Override
    public void removeParticipant(UUID participantId, Long sourceUserId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));
        User sourceUser = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));

        if (!Objects.equals(participant.getUser().getId(), sourceUser.getId()) ||
                !Objects.equals(participant.getGroup().getOrganizer().getId(), sourceUser.getId())) {
            throw new RemoveParticipantAccessDeniedException(participant.getGroup().getId(), sourceUserId);
        }

        exclusionRepository.deleteByGiverOrReceiver(participant, participant);
        participantRepository.delete(participant);
    }

    @Transactional
    @Override
    public void addExclusion(Long sourceUserId, UUID groupId, UUID giverId, UUID receiverId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Participant giver = participantRepository.findById(giverId)
                .orElseThrow(() -> new ParticipantNotFoundException(giverId));

        Participant receiver = participantRepository.findById(receiverId)
                .orElseThrow(() -> new ParticipantNotFoundException(receiverId));

        if (exclusionRepository.existsByGiverAndReceiver(giver, receiver)) {
            throw new ExclusionAlreadyExistsException(giverId, receiverId);
        }

        if (!canAddExclusion(sourceUserId, group, giver, receiver)) {
            throw new IncorrectExclusionException(sourceUserId, giverId, receiverId);
        }

        Exclusion exclusion = new Exclusion();
        exclusion.setGiver(giver);
        exclusion.setReceiver(receiver);
        exclusion.setGroup(group);

        exclusionRepository.save(exclusion);
    }

    @Transactional
    @Override
    public void deleteExclusion(Long sourceUserId, UUID groupId, UUID exclusionId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Exclusion exclusion = exclusionRepository.findById(exclusionId)
                .orElseThrow(() -> new ExclusionNotFoundException(exclusionId));

        if (!canGetOrDeleteExclusion(sourceUserId, group, exclusion)) {
            throw new DeleteExclusionAccessDeniedException(exclusionId, groupId, sourceUserId);
        }

        exclusionRepository.delete(exclusion);
    }

    @Transactional
    @Override
    public ExclusionDto getExclusion(Long sourceUserId, UUID groupId, UUID exclusionId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Exclusion exclusion = exclusionRepository.findById(exclusionId)
                .orElseThrow(() -> new ExclusionNotFoundException(exclusionId));

        if (!canGetOrDeleteExclusion(sourceUserId, group, exclusion)) {
            throw new GetExclusionAccessDeniedException(exclusionId, groupId, sourceUserId);
        }

        return ExclusionDto.fromEntity(exclusion);
    }

    @Transactional
    @Override
    public ListExclusionDto getExclusions(Long sourceUserId, UUID groupId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        if (!Objects.equals(group.getOrganizer().getId(), sourceUserId)) {
            throw new GetExclusionsAccessDeniedException(groupId, sourceUserId);
        }

        List<Exclusion> exclusions = group.getExclusions();
        return new ListExclusionDto(
                exclusions.size(),
                exclusions.stream().map(ExclusionDto::fromEntity).toList()
        );
    }

    @Transactional
    @Override
    public JoinLinkResponse getJoinLink(UUID groupId, Long sourceUserId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User user = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));

        JoinLinkResponse result = new JoinLinkResponse(
                "POST",
                String.format("/api/groups/%s/participants", groupId)
        );

        if (Objects.equals(group.getOrganizer().getId(), user.getId())) {
            return result;
        }

        if (!participantRepository.existsByGroupAndUser(group, user)) {
            throw new GetJoinLinkAccessDeniedException(groupId, user.getId());
        }

        return result;
    }

    @Transactional
    @Override
    public void markDone(UUID groupId, Long sourceUserId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User user = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));

        if (!Objects.equals(group.getOrganizer().getId(), user.getId())) {
            throw new MarkDoneAccessDeniedException(groupId, user.getId());
        }

        group.setDone(true);
        groupRepository.save(group);
    }

    @Override
    public List<SantaGroup> getAllOrganizedByUser(Long userId) {
        return groupRepository.findAllByOrganizer_Id(userId);
    }

    @Override
    public List<SantaGroup> getAllParticipatedByUser(Long userId) {
        return groupRepository.findAllWherePartition(userId);
    }

    @Transactional
    @Override
    public UUID getParticipantId(UUID groupId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Participant participant = participantRepository
                .findByGroup_IdAndUser_Id(groupId, userId)
                .orElseThrow(() -> new ParticipantNotFoundException(groupId, userId));


        return participant.getId();
    }

    private boolean canAddExclusion(Long sourceUserId, SantaGroup group, Participant giver, Participant receiver) {
        if (giver.getId() == receiver.getId()) {
            return false;
        }
        if (Objects.equals(group.getOrganizer().getId(), sourceUserId)) {
            return true;
        }
        return Objects.equals(giver.getUser().getId(), sourceUserId);
    }

    private boolean canGetOrDeleteExclusion(Long sourceUserId, SantaGroup group, Exclusion exclusion) {
        if (exclusion.getGroup().getId() != group.getId()) {
            return false;
        }
        if (Objects.equals(group.getOrganizer().getId(), sourceUserId)) {
            return true;
        }
        return Objects.equals(exclusion.getGiver().getUser().getId(), sourceUserId);
    }
}
