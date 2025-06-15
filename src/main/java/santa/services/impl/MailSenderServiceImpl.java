package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import santa.dtos.MailDto;
import santa.services.MailSenderService;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender mailSender;

    @Override
    public void send(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.to());
        message.setFrom(mailDto.from());
        message.setSubject(mailDto.subject());
        message.setText(mailDto.text());
        mailSender.send(message);
    }
}
