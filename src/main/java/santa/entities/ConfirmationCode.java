package santa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "t_confirmation_codes")
@Data
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Instant createdAt;

    private Instant expiresAt;

    @OneToOne
    private User user;
}
