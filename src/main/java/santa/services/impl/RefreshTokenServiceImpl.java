package santa.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.SantaConfig;
import santa.entities.RefreshToken;
import santa.entities.User;
import santa.exceptions.RefreshTokenExpiredException;
import santa.exceptions.RefreshTokenNotFoundException;
import santa.repositories.RefreshTokenRepository;
import santa.repositories.UserRepository;
import santa.services.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final SantaConfig santaConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId).orElseThrow();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(expiration()));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException();
        }
        return token;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(token));
    }

    @Transactional
    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    private int expiration() {
        return santaConfig.refresh().expiration();
    }
}
