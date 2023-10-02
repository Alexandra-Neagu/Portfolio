package reservations.validators;

import java.security.InvalidParameterException;
import reservations.entities.Reservation;

public abstract class BaseValidator implements Validator {
    private Validator next;

    @Override
    public void setNext(Validator handler) {
        this.next = handler;
    }

    protected boolean checkNext(UncheckedReservation reservation) throws InvalidParameterException {
        if (next == null) {
            return true;
        }
        return next.handle(reservation);
    }
}
