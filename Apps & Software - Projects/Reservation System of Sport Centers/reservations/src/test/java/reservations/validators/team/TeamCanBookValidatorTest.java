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
public class TeamCanBookValidatorTest {
    @Mock
    public UsersClient usersClient;

    @Mock
    public Validator next;

    @Autowired
    @InjectMocks
    TeamCanBookValidator validator;

    String timestampString = "2022-01-05T18:00:00.000+01:00";
    UUID reservationTeamUuid = UUID.fromString("e27b008e-6e48-11ec-90d6-0242ac120003");
    UncheckedReservation reservation =
            new UncheckedReservation(null, reservationTeamUuid, timestampString, 1);

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenCommunicationIsNotSuccessful() {
        when(usersClient.canTeamBook(reservationTeamUuid, timestampString)).thenReturn(
                new ResponseEntity<>(HttpStatus.BAD_GATEWAY)
        );

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).canTeamBook(reservationTeamUuid, timestampString);
        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_ThrowsException_WhenTeamCannotBook() {
        when(usersClient.canTeamBook(reservationTeamUuid, timestampString)).thenReturn(
                ResponseEntity.of(Optional.of(false))
        );

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).canTeamBook(reservationTeamUuid, timestampString);
        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_ReturnsTrue_WhenTeamCanBook_AndNextReturnsTrue() {
        when(usersClient.canTeamBook(reservationTeamUuid, timestampString)).thenReturn(
                ResponseEntity.of(Optional.of(true))
        );

        when(next.handle(reservation)).thenReturn(true);

        boolean result = validator.handle(reservation);
        assertTrue(result);
        verify(usersClient, times(1)).canTeamBook(reservationTeamUuid, timestampString);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ReturnsFalse_WhenTeamCanBook_AndNextReturnsFalse() {
        when(usersClient.canTeamBook(reservationTeamUuid, timestampString)).thenReturn(
                ResponseEntity.of(Optional.of(true))
        );

        when(next.handle(reservation)).thenReturn(false);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        verify(usersClient, times(1)).canTeamBook(reservationTeamUuid, timestampString);
        verify(next, times(1)).handle(reservation);
    }
}
