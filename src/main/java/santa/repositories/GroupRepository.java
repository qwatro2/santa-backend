package santa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import santa.entities.SantaGroup;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<SantaGroup, UUID> {
    List<SantaGroup> findAllByOrganizer_Id(Long userId);

    @Query("""
        select g
        from SantaGroup g
        join g.participants p
        join p.user u
        where u.id = :userId
    """)
    List<SantaGroup> findAllWherePartition(@Param("userId") Long userId);
}
