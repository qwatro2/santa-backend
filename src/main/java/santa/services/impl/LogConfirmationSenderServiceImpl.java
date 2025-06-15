package santa.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.entities.ConfirmationCode;
import santa.entities.User;
import santa.services.ConfirmationCodeService;
import santa.services.ConfirmationSenderService;

@Service
@ConditionalOnProperty(prefix = "app.confirmation", name = "transport", havingValue = "log", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class LogConfirmationSenderServiceImpl implements ConfirmationSenderService {
    private final ConfirmationCodeService confirmationCodeService;

    @Override
    public void sendConfirmation(User user) {
        ConfirmationCode code = confirmationCodeService.create(user);
        log.atInfo().log(String.format("Email: %s, Code: %s", user.getEmail(), code.getCode()));
    }
}
