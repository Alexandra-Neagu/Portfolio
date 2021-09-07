package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.BannedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Banned ip repository.
 */
@Repository("BannedIpRepository")
public interface BannedIpRepository extends JpaRepository<BannedIp, Long> {
}
