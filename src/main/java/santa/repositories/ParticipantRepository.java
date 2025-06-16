package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import santa.entities.Participant;
import santa.entities.SantaGroup;
import santa.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    boolean existsByGroupAndUser(SantaGroup group, User user);
    Optional<Participant> findByGroup_IdAndUser_Id(UUID groupId, Long userId);
}
