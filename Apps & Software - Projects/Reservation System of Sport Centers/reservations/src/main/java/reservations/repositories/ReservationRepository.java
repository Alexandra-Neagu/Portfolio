package reservations.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reservations.entities.Bookable;
import reservations.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findAllByBookableAndStartTimeBetween(
            Bookable bookable,
            Timestamp startTime,
            Timestamp endTime
    );

    List<Reservation> findAllByStartTimeBetween(
            Timestamp overlappingReservationStart,
            Timestamp overlappingReservationEnd
    );

    List<Reservation> findAllByBookable(Bookable bookable);

    List<Reservation> findAllByTeamUUIDAndStartTimeAfter(UUID teamUUID, Timestamp currentTimestamp);

    List<Reservation> findAllByTeamUUID(UUID teamUuid);
}
