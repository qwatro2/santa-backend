package santa.services;

import santa.entities.User;

public interface ConfirmationSenderService {
    void sendConfirmation(User user);
}
