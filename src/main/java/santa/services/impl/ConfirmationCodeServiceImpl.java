package santa.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.SantaConfig;
import santa.entities.ConfirmationCode;
import santa.entities.User;
import santa.repositories.ConfirmationCodeRepository;
import santa.services.ConfirmationCodeService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final SantaConfig santaConfig;
    private final SecureRandom secureRandom;

    @Transactional
    @Override
    public ConfirmationCode create(User user) {
        ConfirmationCode code = confirmationCodeRepository.findByUser(user)
                .orElse(new ConfirmationCode());
        code.setCode(randomCode());
        code.setCreatedAt(Instant.now());
        code.setExpiresAt(code.getCreatedAt().plusMillis(santaConfig.confirmation().expiration()));
        code.setUser(user);
        return confirmationCodeRepository.save(code);
    }

    private String randomCode() {
        int length = santaConfig.confirmation().length();
        String alphabet = santaConfig.confirmation().alphabet();

        return secureRandom.ints(length, 0, alphabet.length())
                .mapToObj(alphabet::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
