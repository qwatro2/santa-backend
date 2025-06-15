package santa;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public record SantaConfig(int randomSeed,
                          boolean handleAllExceptions,
                          int shuffleMaxAttempts,
                          SantaNotificationConfig notification,
                          SantaJwtConfig jwt,
                          SantaRefreshConfig refresh,
                          SantaAvatarsConfig avatars,
                          SantaConfirmationConfig confirmation,
                          SantaMailConfig mail
) {
    public record SantaNotificationConfig(String type, SantaRemindersConfig reminders,
                                          SantaScheduleConfig schedule) {
        public record SantaRemindersConfig(List<Integer> days) {
        }

        public record SantaScheduleConfig(String time) {
        }
    }

    public record SantaJwtConfig(String secret, int expiration) {
    }

    public record SantaRefreshConfig(int expiration) {
    }

    public record SantaAvatarsConfig(String storagePath) {
    }

    public record SantaConfirmationConfig(int expiration, String transport, int length, String alphabet) {
    }

    public record SantaMailConfig(SantaLetterConfig confirm, SantaLetterConfig notification) {
        public record SantaLetterConfig(String subject, TextPattern text, String from) {
            public record TextPattern(String pattern, int numberOfVariables) {
            }
        }
    }
}
