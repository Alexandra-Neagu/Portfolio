package reservations.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reservations.entities.Bookable;

@Repository
public interface BookableRepository extends JpaRepository<Bookable, UUID> {
}