package santa;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public record SantaConfig(boolean handleAllExceptions,
                          int shuffleMaxAttempts,
                          SantaNotificationConfig notification,
                          SantaJwtConfig jwt,
                          SantaRefreshConfig refresh,
                          SantaAvatarsConfig avatars) {
    public record SantaNotificationConfig(String type, KafkaConfig kafka, SantaRemindersConfig reminders) {
        public record KafkaConfig(String topic, String host) {
        }

        public record SantaRemindersConfig(List<Integer> days) {
        }
    }

    public record SantaJwtConfig(String secret, int expiration) {
    }

    public record SantaRefreshConfig(int expiration) {
    }

    public record SantaAvatarsConfig(String storagePath) {
    }
}
