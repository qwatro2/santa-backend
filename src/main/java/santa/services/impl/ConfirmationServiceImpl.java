package santa.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import santa.dtos.ConfirmMailRequest;
import santa.entities.ConfirmationCode;
import santa.entities.User;
import santa.exceptions.ConfirmationCodeNotFoundException;
import santa.exceptions.UserNotFoundException;
import santa.exceptions.WrongConfirmationCodeException;
import santa.repositories.ConfirmationCodeRepository;
import santa.repositories.UserRepository;
import santa.services.ConfirmationService;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final UserRepository userRepository;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Transactional
    @Override
    public void confirm(ConfirmMailRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));
        ConfirmationCode confirmationCode = confirmationCodeRepository.findByUser(user)
                .orElseThrow(() -> new ConfirmationCodeNotFoundException(user.getId()));

        if (user.isVerified() ||
                confirmationCode.getExpiresAt().isBefore(Instant.now()) ||
                !Objects.equals(confirmationCode.getCode(), request.code())) {
            throw new WrongConfirmationCodeException(user.getId(), request.code());
        }

        user.setVerified(true);
        userRepository.save(user);
        confirmationCodeRepository.delete(confirmationCode);
    }
}
