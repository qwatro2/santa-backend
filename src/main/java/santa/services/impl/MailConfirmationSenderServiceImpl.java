package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.SantaConfig;
import santa.dtos.MailDto;
import santa.entities.ConfirmationCode;
import santa.entities.User;
import santa.services.ConfirmationCodeService;
import santa.services.ConfirmationSenderService;
import santa.services.MailSenderService;

import java.text.MessageFormat;

@Service
@ConditionalOnProperty(prefix = "app.confirmation", name = "transport", havingValue = "mail")
@RequiredArgsConstructor
public class MailConfirmationSenderServiceImpl implements ConfirmationSenderService {
    private final MailSenderService mailSenderService;
    private final ConfirmationCodeService confirmationCodeService;
    private final SantaConfig santaConfig;

    @Override
    public void sendConfirmation(User user) {
        ConfirmationCode code = confirmationCodeService.create(user);
        MailDto mailDto = construct(user, code);
        mailSenderService.send(mailDto);
    }

    private MailDto construct(User user, ConfirmationCode code) {
        SantaConfig.SantaMailConfig mailConfig = santaConfig.mail();
        return new MailDto(
                user.getEmail(),
                mailConfig.from(),
                mailConfig.subject(),
                MessageFormat.format(mailConfig.text().pattern(), user.getDisplayName(), code.getCode())
        );
    }
}
