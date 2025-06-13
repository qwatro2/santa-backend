package santa.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.entities.User;
import santa.services.NotificationService;

@Service
@ConditionalOnProperty(name = "notification.type", havingValue = "log", matchIfMissing = true)
@Slf4j
public class LogNotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(User user, String message) {
        log.info("Notification for {}: {}", user.getEmail(), message);
    }
}
