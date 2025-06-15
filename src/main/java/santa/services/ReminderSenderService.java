package santa.services;

import santa.entities.SantaGroup;
import santa.entities.User;

public interface ReminderSenderService {
    void sendReminder(User user, SantaGroup group, int daysRemaining);
}
