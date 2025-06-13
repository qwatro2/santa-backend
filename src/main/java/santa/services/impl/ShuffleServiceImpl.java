package santa.services.impl;

import santa.SantaConfig;
import santa.entities.Assignment;
import santa.entities.Exclusion;
import santa.entities.Participant;
import santa.entities.SantaGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.exceptions.AssignmentsGenerationFailedException;
import santa.exceptions.GroupNotFoundException;
import santa.repositories.AssignmentRepository;
import santa.repositories.ExclusionRepository;
import santa.repositories.GroupRepository;
import santa.services.ShuffleService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShuffleServiceImpl implements ShuffleService {
    private final SantaConfig santaConfig;
    private final ExclusionRepository exclusionRepository;
    private final GroupRepository groupRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public List<Assignment> shuffle(UUID groupId) {
        SantaGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        List<Participant> participants = group.getParticipants()
                .stream()
                .filter(Participant::isActive)
                .collect(Collectors.toList());

        Map<Participant, List<Participant>> exclusions = loadExclusions(group);
        List<Participant> receivers = new ArrayList<>(participants);

        int attempts = 0;
        while (attempts++ < santaConfig.shuffleMaxAttempts()) {
            Collections.shuffle(receivers);
            if (isValidAssignment(participants, receivers, exclusions)) {
                List<Assignment> assignments = createAssignments(group, participants, receivers);
                group.setShuffled(true);
                groupRepository.save(group);
                return assignments;
            }
        }
        throw new AssignmentsGenerationFailedException(santaConfig.shuffleMaxAttempts());
    }

    private Map<Participant, List<Participant>> loadExclusions(SantaGroup group) {
        return exclusionRepository.findByGroup(group).stream()
                .collect(Collectors.groupingBy(
                        Exclusion::getGiver,
                        Collectors.mapping(Exclusion::getReceiver, Collectors.toList())
                ));
    }

    private boolean isValidAssignment(List<Participant> givers,
                                      List<Participant> receivers,
                                      Map<Participant, List<Participant>> exclusions) {
        for (int i = 0; i < givers.size(); i++) {
            Participant giver = givers.get(i);
            Participant receiver = receivers.get(i);

            if (giver.equals(receiver)) return false;
            if (exclusions.getOrDefault(giver, List.of()).contains(receiver)) return false;
        }
        return true;
    }

    private List<Assignment> createAssignments(SantaGroup group,
                                               List<Participant> givers,
                                               List<Participant> receivers) {
        List<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < givers.size(); i++) {
            Assignment assignment = new Assignment();
            assignment.setSanta(givers.get(i));
            assignment.setReceiver(receivers.get(i));
            assignment.setGroup(group);
            assignments.add(assignment);
            assignmentRepository.save(assignment);
        }
        return assignments;
    }
}
