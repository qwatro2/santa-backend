package santa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "t_avatars")
@Data
public class Avatar {
    @PreRemove
    private void preRemove() {
        if (user != null) {
            user.setAvatar(null);
        }
    }

    @Id
    private UUID id;
    private String extension;

    @OneToOne
    private User user;
}
