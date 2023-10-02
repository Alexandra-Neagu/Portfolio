package reservations.validators.equipment;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.List;
import reservations.entities.Bookable;
import reservations.entities.Reservation;
import reservations.repositories.ReservationRepository;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This validator checks whether the amount of a piece of equipment
 * is available for booking.
 */
public class EquipmentIsAvailableValidator extends BaseValidator {
    private final ReservationRepository reservationRepository;

    public EquipmentIsAvailableValidator(ReservationRepository repository) {
        this.reservationRepository = repository;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        Timestamp startTimestamp = reservation.getConvertedTimestamp();
        Bookable equipment = reservation.getConvertedBookable();
        int amount = reservation.getAmount();
        long millisecondsInHour = 3_600_000L;

        Timestamp overlappingReservationStart =
                new Timestamp(startTimestamp.getTime() - millisecondsInHour + 1);
        Timestamp overlappingReservationEnd =
                new Timestamp(startTimestamp.getTime() + millisecondsInHour - 1);

        List<Reservation> reservationsContainingEquipment =
                reservationRepository.findAllByBookableAndStartTimeBetween(
                        equipment,
                        overlappingReservationStart,
                        overlappingReservationEnd
                );

        int amountOfBookedEquipment =
                reservationsContainingEquipment.stream().mapToInt(Reservation::getAmount).sum();

        if (amountOfBookedEquipment + amount > equipment.getMaxCapacity()) {
            throw new InvalidParameterException("The amount that you requested is not available.");
        }

        return super.checkNext(reservation);
    }
}
