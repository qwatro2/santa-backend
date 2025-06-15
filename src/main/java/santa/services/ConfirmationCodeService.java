package santa.services;

import santa.entities.ConfirmationCode;
import santa.entities.User;

public interface ConfirmationCodeService {
    ConfirmationCode create(User user);
}
