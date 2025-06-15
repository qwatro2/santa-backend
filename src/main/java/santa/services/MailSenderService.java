package santa.services;

import santa.dtos.MailDto;

public interface MailSenderService {
    void send(MailDto mailDto);
}
