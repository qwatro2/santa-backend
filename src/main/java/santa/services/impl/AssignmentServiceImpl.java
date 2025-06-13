package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.entities.Assignment;
import santa.exceptions.AssignmentNotFoundException;
import santa.repositories.AssignmentRepository;
import santa.services.AssignmentService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Override
    public Assignment findByGroupAndSantaUser(UUID groupId, Long userId) {
        return assignmentRepository.findByGroup_IdAndSanta_User_Id(groupId, userId)
                .orElseThrow(() -> new AssignmentNotFoundException(groupId, userId));
    }
}
