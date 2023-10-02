package reservations.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reservations.entities.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {
}
