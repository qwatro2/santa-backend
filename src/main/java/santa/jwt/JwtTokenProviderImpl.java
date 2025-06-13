package santa.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import santa.SantaConfig;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final SantaConfig santaConfig;

    @Override
    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        return generateTokenFromUserId(userPrincipal.getId());
    }

    @Override
    public String generateTokenFromUserId(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration());

        return Jwts.builder()
                .subject(Long.toString(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (Exception ex) {
            if (ex instanceof ExpiredJwtException expiredJwtException) {
                throw expiredJwtException;
            }
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private int expiration() {
        return santaConfig.jwt().expiration();
    }

    private String secret() {
        return santaConfig.jwt().secret();
    }
}

