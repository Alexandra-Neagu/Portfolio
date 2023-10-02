package reservations.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reservations.entities.SportsFacility;

@Repository
public interface SportsFacilityRepository extends JpaRepository<SportsFacility, UUID> {
}
