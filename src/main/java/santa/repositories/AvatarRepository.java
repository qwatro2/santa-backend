package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import santa.entities.Assignment;
import santa.entities.Avatar;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, UUID> {
    Optional<Avatar> findByUser_Id(Long userId);
}
