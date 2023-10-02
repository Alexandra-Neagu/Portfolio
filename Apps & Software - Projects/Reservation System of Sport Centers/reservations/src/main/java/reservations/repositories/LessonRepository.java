package reservations.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reservations.entities.Lesson;
import reservations.entities.SportsFacility;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findDistinctBySportsFacilityAndStartTimeBetween(
            SportsFacility facility, Timestamp start, Timestamp end
    );

    List<Lesson> findAllByStartTimeAfterAndEndTimeBefore(Timestamp start, Timestamp end);
}