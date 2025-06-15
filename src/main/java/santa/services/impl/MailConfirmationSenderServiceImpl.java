package santa.services.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import santa.entities.User;
import santa.services.ConfirmationSenderService;

@Service
@ConditionalOnProperty(prefix = "app.confirmation", name = "transport", havingValue = "mail")
public class MailConfirmationSenderServiceImpl implements ConfirmationSenderService {
    @Override
    public void sendConfirmation(User user) {
        throw new NotImplementedException();
    }
}
