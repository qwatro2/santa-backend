package santa.services;

import santa.entities.User;

public interface NotificationService {
    void sendNotification(User user, String message);
}
