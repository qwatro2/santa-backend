package santa.services;

import santa.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshToken findByToken(String token);

    void deleteByToken(String token);
}
