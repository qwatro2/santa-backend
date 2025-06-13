package santa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "t_assignments")
@Data
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Participant santa;

    @ManyToOne
    private Participant receiver;

    @ManyToOne
    private SantaGroup group;
}
