package santa.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.entities.User;
import santa.services.NotificationService;

@Service
@ConditionalOnProperty(name = "notification.type", havingValue = "mail")
@Slf4j
public class MailNotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(User user, String message) {
        throw new NotImplementedException();
    }
}
