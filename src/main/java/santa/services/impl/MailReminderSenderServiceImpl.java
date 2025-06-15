package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.SantaConfig;
import santa.dtos.MailDto;
import santa.entities.SantaGroup;
import santa.entities.User;
import santa.services.MailSenderService;
import santa.services.ReminderSenderService;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@ConditionalOnProperty(prefix = "app.notification", name = "type", havingValue = "mail")
@RequiredArgsConstructor
public class MailReminderSenderServiceImpl implements ReminderSenderService {
    private final MailSenderService mailSenderService;
    private final SantaConfig santaConfig;

    @Override
    public void sendReminder(User user, SantaGroup group, int daysRemaining) {
        MailDto mailDto = construct(user, group, daysRemaining);
        mailSenderService.send(mailDto);
    }

    private MailDto construct(User user, SantaGroup group, int daysRemaining) {
        return new MailDto(
                user.getEmail(),
                santaConfig.mail().notification().from(),
                santaConfig.mail().notification().subject(),
                formatText(user.getDisplayName(), group.getExchangeDate(), group.getName(), daysRemaining)
        );
    }

    private String formatText(String name, Instant exchangeDate, String groupName, int daysRemaining) {
        String formatedExchangeDate = LocalDateTime.ofInstant(exchangeDate, ZoneId.systemDefault())
                .toLocalDate().format(DateTimeFormatter.ofPattern("d MMM y", Locale.forLanguageTag("ru")));
        return MessageFormat.format(santaConfig.mail().notification().text().pattern(),
                name, formatedExchangeDate, groupName, formatDays(daysRemaining));
    }

    private String formatDays(int days) {
        String s;
        if (11 <= days && days <= 14) {
            s = "дней";
        } else if (days % 10 == 1) {
            s = "день";
        } else if (2 <= days % 10 && days % 10 <= 4) {
            s = "дня";
        } else {
            s = "дней";
        }
        return String.format("%d %s", days, s);
    }
}
