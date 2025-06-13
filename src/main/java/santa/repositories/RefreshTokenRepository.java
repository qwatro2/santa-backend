package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import santa.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
