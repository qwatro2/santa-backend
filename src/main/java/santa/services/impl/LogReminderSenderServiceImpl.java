package santa.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.entities.SantaGroup;
import santa.entities.User;
import santa.services.ReminderSenderService;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@ConditionalOnProperty(prefix = "app.notification", name = "type", havingValue = "log", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class LogReminderSenderServiceImpl implements ReminderSenderService {
    @Override
    public void sendReminder(User user, SantaGroup group, int daysRemaining) {
        log.atInfo().log(String.format(
                "Hi, %s! It's exchanging in group %s in %s. %d days remaining!!!",
                user.getDisplayName(),
                group.getName(),
                LocalDateTime.ofInstant(group.getExchangeDate(), ZoneId.systemDefault()).toLocalDate().toString(),
                daysRemaining
        ));
    }
}
