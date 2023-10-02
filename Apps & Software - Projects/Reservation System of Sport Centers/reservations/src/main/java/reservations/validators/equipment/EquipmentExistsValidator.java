package reservations.validators.equipment;

import java.security.InvalidParameterException;
import reservations.entities.Equipment;
import reservations.entities.Reservation;
import reservations.repositories.EquipmentRepository;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * Checks whether there is an equipment with the bookable uuid
 * provided in the Unchecked reservation object. If the object does not
 * exist, an exception is thrown.
 */
public class EquipmentExistsValidator extends BaseValidator {
    private final EquipmentRepository equipmentRepository;

    public EquipmentExistsValidator(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        Equipment equipment = equipmentRepository.findById(reservation.getBookableUuid())
                .orElseThrow(() -> new InvalidParameterException("The equipment doesn't exist."));

        reservation.setConvertedBookable(equipment);
        return super.checkNext(reservation);
    }
}
