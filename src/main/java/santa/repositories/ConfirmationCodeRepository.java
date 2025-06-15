package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import santa.entities.ConfirmationCode;
import santa.entities.User;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByUser(User user);
}
