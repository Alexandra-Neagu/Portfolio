package reservations.validators;

import java.security.InvalidParameterException;
import reservations.entities.Reservation;


public interface Validator {
    void setNext(Validator handler);

    boolean handle(UncheckedReservation reservation) throws InvalidParameterException;
}
