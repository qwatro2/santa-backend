package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import santa.entities.Assignment;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    Optional<Assignment> findByGroup_IdAndSanta_User_Id(UUID groupId, Long santaUserId);
}
