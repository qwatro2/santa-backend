package santa.dtos;

public record MailDto(
        String to,
        String from,
        String subject,
        String text
) {
}
