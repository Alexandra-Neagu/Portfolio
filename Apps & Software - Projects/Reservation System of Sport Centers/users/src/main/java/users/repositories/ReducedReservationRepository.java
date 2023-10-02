package users.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.entities.ReducedReservation;

/**
 * The Reduced Reservation repository.
 */
@Repository
public interface ReducedReservationRepository extends JpaRepository<ReducedReservation, UUID> {
}
