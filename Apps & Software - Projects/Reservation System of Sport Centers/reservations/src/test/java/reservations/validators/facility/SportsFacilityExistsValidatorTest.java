package reservations.validators.facility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reservations.entities.SportsFacility;
import reservations.repositories.SportsFacilityRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class SportsFacilityExistsValidatorTest {
    @Mock
    SportsFacilityRepository sportsFacilityRepository;

    @Mock
    Validator next;

    @Autowired
    @InjectMocks
    SportsFacilityExistsValidator validator;

    SportsFacility facility = new SportsFacility("testFacility", 2, 4);
    UncheckedReservation reservation =
            new UncheckedReservation(facility.getUuid(), null, null, 1);

    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenFacilityDoesNotExist() {
        when(sportsFacilityRepository.findById(facility.getUuid())).thenReturn(Optional.empty());
        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
    }

    @Test
    public void handleReturnsTrue_WhenFacilityExists_AndNextReturnsTrue() {
        when(sportsFacilityRepository.findById(facility.getUuid()))
                .thenReturn(Optional.of(facility));

        when(next.handle(reservation)).thenReturn(true);

        boolean result = validator.handle(reservation);
        assertTrue(result);
        assertEquals(facility, reservation.getConvertedBookable());
    }

    @Test
    public void handleReturnsFalse_WhenEquipmentExists_AndNextReturnsFalse() {
        when(sportsFacilityRepository.findById(facility.getUuid()))
                .thenReturn(Optional.of(facility));

        when(next.handle(reservation)).thenReturn(false);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        assertEquals(facility, reservation.getConvertedBookable());
    }
}
