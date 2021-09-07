package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Moderator repository.
 */
@Repository("ModeratorRepository")
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {
}
