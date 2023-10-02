package users.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.entities.Team;

/**
 * The Team repository.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
}
