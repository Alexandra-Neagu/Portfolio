package authentication.repositories;

import authentication.entities.UserCredential;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {

    /**
     * Find user by username.
     *
     * @param username username
     * @return user information from matching user
     */
    public UserCredential findByUsername(String username);

}
