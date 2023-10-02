package reservations.validators.facility;

import java.security.InvalidParameterException;
import reservations.entities.SportsFacility;
import reservations.repositories.SportsFacilityRepository;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * Checks whether there is a sports facility with the bookable uuid
 * provided in the Unchecked reservation object. If the object does not
 * exist, an exception is thrown.
 */
public class SportsFacilityExistsValidator extends BaseValidator {
    private final SportsFacilityRepository sportsFacilityRepository;

    public SportsFacilityExistsValidator(SportsFacilityRepository sportsFacilityRepository) {
        this.sportsFacilityRepository = sportsFacilityRepository;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        SportsFacility facility = sportsFacilityRepository.findById(reservation.getBookableUuid())
                .orElseThrow(
                        () -> new InvalidParameterException("The sports facility doesn't exist.")
                );

        reservation.setConvertedBookable(facility);
        return super.checkNext(reservation);
    }
}
