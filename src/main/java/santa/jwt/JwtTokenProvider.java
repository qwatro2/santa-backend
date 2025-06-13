package santa.jwt;

import org.springframework.security.core.Authentication;

public interface JwtTokenProvider {
    String generateToken(Authentication authentication);

    String generateTokenFromUserId(Long userId);

    Long getUserIdFromJWT(String token);

    boolean validateToken(String authToken);
}
