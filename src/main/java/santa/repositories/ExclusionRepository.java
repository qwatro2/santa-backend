package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import santa.entities.Exclusion;
import santa.entities.Participant;
import santa.entities.SantaGroup;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExclusionRepository extends JpaRepository<Exclusion, UUID> {
    List<Exclusion> findByGroup(SantaGroup group);

    void deleteByGiverOrReceiver(Participant giver, Participant receiver);

    boolean existsByGiverAndReceiver(Participant giver, Participant receiver);
}
