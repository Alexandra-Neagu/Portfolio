package users.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.entities.Team;
import users.entities.User;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
