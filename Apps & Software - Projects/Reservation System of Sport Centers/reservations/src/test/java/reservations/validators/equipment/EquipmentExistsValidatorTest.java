package reservations.validators.equipment;

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
import reservations.entities.Equipment;
import reservations.repositories.EquipmentRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class EquipmentExistsValidatorTest {
    @Mock
    EquipmentRepository equipmentRepository;

    @Mock
    Validator next;

    @Autowired
    @InjectMocks
    EquipmentExistsValidator validator;

    Equipment equipment = new Equipment("test", 2, "test");
    UncheckedReservation reservation =
            new UncheckedReservation(equipment.getUuid(), null, null, 1);

    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenEquipmentDoesNotExist() {
        when(equipmentRepository.findById(equipment.getUuid())).thenReturn(Optional.empty());
        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
    }

    @Test
    public void handleReturnsTrue_WhenEquipmentExists_AndNextReturnsTrue() {
        when(equipmentRepository.findById(equipment.getUuid())).thenReturn(Optional.of(equipment));
        when(next.handle(reservation)).thenReturn(true);

        boolean result = validator.handle(reservation);
        assertTrue(result);
        assertEquals(equipment, reservation.getConvertedBookable());
    }

    @Test
    public void handleReturnsFalse_WhenEquipmentExists_AndNextReturnsFalse() {
        when(equipmentRepository.findById(equipment.getUuid())).thenReturn(Optional.of(equipment));
        when(next.handle(reservation)).thenReturn(false);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        assertEquals(equipment, reservation.getConvertedBookable());
    }
}
