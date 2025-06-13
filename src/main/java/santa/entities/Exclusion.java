package santa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "t_exclusions")
@Data
public class Exclusion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Participant giver;

    @ManyToOne
    private Participant receiver;

    @ManyToOne
    private SantaGroup group;
}
