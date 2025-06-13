package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import santa.SantaConfig;
import santa.dtos.NotificationDto;
import santa.entities.User;
import santa.services.NotificationService;

@Service
@ConditionalOnProperty(name = "notification.type", havingValue = "kafka")
@RequiredArgsConstructor
public class KafkaNotificationServiceImpl implements NotificationService {
    private final SantaConfig santaConfig;
    private final KafkaTemplate<Long, NotificationDto> kafkaTemplate;

    @Override
    public void sendNotification(User user, String message) {
        NotificationDto dto = new NotificationDto(user.getId(), message);
        kafkaTemplate.send(santaConfig.notification().kafka().topic(), dto);
    }
}
