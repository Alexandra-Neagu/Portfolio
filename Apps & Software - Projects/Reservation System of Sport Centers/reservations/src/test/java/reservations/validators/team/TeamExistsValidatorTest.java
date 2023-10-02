package reservations.validators.team;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class TeamExistsValidatorTest {
    @Mock
    public UsersClient usersClient;

    @Mock
    public Validator next;

    @Autowired
    @InjectMocks
    TeamExistsValidator validator;

    UUID reservationTeamUuid = UUID.fromString("e27b008e-6e48-11ec-90d6-0242ac120003");
    UncheckedReservation reservation =
            new UncheckedReservation(null, reservationTeamUuid, null, 1);

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenCommunicationIsNotSuccessful() {
        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).doesTeamExist(reservationTeamUuid);
        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_ThrowsException_WhenTeamDoesNotExist() {
        when(usersClient.doesTeamExist(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(false))
        );

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).doesTeamExist(reservationTeamUuid);
        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_ReturnsTrue_WhenTeamExists_AndNextReturnsTrue() {
        when(usersClient.doesTeamExist(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(true))
        );

        when(next.handle(reservation)).thenReturn(true);

        boolean result = validator.handle(reservation);
        assertTrue(result);
        verify(usersClient, times(1)).doesTeamExist(reservationTeamUuid);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ReturnsFalse_WhenTeamCanBook_AndNextReturnsFalse() {
        when(usersClient.doesTeamExist(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(true))
        );

        when(next.handle(reservation)).thenReturn(false);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        verify(usersClient, times(1)).doesTeamExist(reservationTeamUuid);
        verify(next, times(1)).handle(reservation);
    }
}
