package santa.services;

import santa.dtos.ConfirmMailRequest;

public interface ConfirmationService {
    void confirm(ConfirmMailRequest request);
}
