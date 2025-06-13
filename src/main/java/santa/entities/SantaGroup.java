package santa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_groups")
@Data
public class SantaGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Instant exchangeDate;
    private Integer costMin;
    private Integer costMax;
    private boolean shuffled = false;
    private boolean done = false;

    @ManyToOne
    private User organizer;

    @OneToMany(mappedBy = "group")
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Exclusion> exclusions = new ArrayList<>();
}
