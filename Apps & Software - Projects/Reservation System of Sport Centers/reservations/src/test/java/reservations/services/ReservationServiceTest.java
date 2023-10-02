package reservations.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reservations.entities.Bookable;
import reservations.entities.Reservation;
import reservations.repositories.BookableRepository;
import reservations.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookableRepository bookableRepository;

    @Autowired
    @InjectMocks
    private ReservationService reservationService;

    Bookable bookable = new Bookable("name", 4);

    UUID uuid = UUID.fromString("12345678-1234-1234-1234-123456789012");
    UUID reservationTeamUuid = UUID.fromString("12345678-abcd-ef12-3456-0123456789ab");

    Timestamp timestamp1;
    Timestamp timestamp2;
    Timestamp timestamp3;

    Reservation reservation;
    Reservation reservation2;
    Reservation reservation3;

    String incorrectReservationString = "Reservation retrieved in incorrect order";
    
    /**
     * Set up the timestamps.
     */
    @BeforeEach
    public void setup() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        timestamp1 = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.HOUR, 1);
        timestamp2 = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR, 5);
        timestamp3 = new Timestamp(calendar.getTimeInMillis());

        reservation = new Reservation(reservationTeamUuid, bookable, timestamp1);
        reservation2 = new Reservation(uuid, bookable, timestamp2);
        reservation3 = new Reservation(uuid, bookable, timestamp3);
    }

    @Test
    public void getReservation_ReturnsCorrectReservation_WhenItExists() {
        when(reservationRepository.findById(reservation.getReservationUUID()))
                .thenReturn(Optional.of(reservation));

        Reservation returnedReservation =
                reservationService.getReservation(reservation.getReservationUUID());

        assertEquals("Incorrect reservation is returned", reservation, returnedReservation);
    }

    @Test
    public void getReservation_ThrowsException_WhenReservationDoesNotExist() {
        when(reservationRepository.findById(reservation.getReservationUUID()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> reservationService.getReservation(reservation.getReservationUUID())
        );
    }

    @Test
    public void doesReservationExists_ReturnsFalse_WhenItDoesNotExist() {
        when(reservationRepository.findById(reservation.getReservationUUID()))
                .thenReturn(Optional.empty());

        boolean result = reservationService.doesReservationExist(
                reservation.getReservationUUID(),
                reservationTeamUuid
        );

        assertFalse(result);
    }

    @Test
    public void doesReservationExists_ReturnsFalse_WhenTeamIdsDoNotMatch() {
        when(reservationRepository.findById(reservation.getReservationUUID()))
                .thenReturn(Optional.of(reservation));

        UUID otherTeamUuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        boolean result = reservationService.doesReservationExist(
                reservation.getReservationUUID(),
                otherTeamUuid
        );

        assertFalse(result);
    }

    @Test
    public void doesReservationExist_ReturnsTrue_WhenEverythingIsCorrect() {
        when(reservationRepository.findById(reservation.getReservationUUID()))
                .thenReturn(Optional.of(reservation));

        boolean result = reservationService.doesReservationExist(
                reservation.getReservationUUID(),
                reservationTeamUuid
        );

        assertTrue("Result should have been true", result);
    }

    @Test
    public void getBookable_ReturnsCorrectEntity_WhenValuesAreValid() {
        UUID id = bookable.getUuid();

        when(bookableRepository.findById(id)).thenReturn(Optional.of(bookable));

        Bookable returnedBookable = reservationService.getBookable(id);

        assertEquals(
                "Different bookable than the one requested is returned",
                bookable,
                returnedBookable
        );

        verify(bookableRepository, times(1)).findById(id);
    }

    @Test
    public void getBookable_ThrowsException_WhenNoSuchBookableExists() {
        UUID id = bookable.getUuid();

        when(bookableRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> reservationService.getBookable(id)
        );

        verify(bookableRepository, times(1)).findById(id);
    }

    @Test
    public void retrieveReservationsByBookable_ReturnsReservationsInCorrectOrder() {
        List<Reservation> reservations = new ArrayList<>();

        reservations.add(reservation2);
        reservations.add(reservation3);
        reservations.add(reservation);

        UUID id = bookable.getUuid();
        when(bookableRepository.findById(id)).thenReturn(Optional.of(bookable));
        when(reservationRepository.findAllByBookable(bookable)).thenReturn(reservations);

        List<Reservation> returnedReservations =
                reservationService.retrieveReservationsByBookable(id);

        assertEquals(incorrectReservationString, reservation3, returnedReservations.get(0));
        assertEquals(incorrectReservationString, reservation2, returnedReservations.get(1));
        assertEquals(incorrectReservationString, reservation, returnedReservations.get(2));

        verify(bookableRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).findAllByBookable(bookable);
    }

    @Test
    public void retrieveAllUpcoming_ReturnsReservationsInCorrectOrder() {
        List<Reservation> reservations = new ArrayList<>();

        reservations.add(reservation2);
        reservations.add(reservation3);
        reservations.add(reservation);

        when(reservationRepository.findAllByTeamUUIDAndStartTimeAfter(eq(uuid), any()))
                .thenReturn(reservations);

        List<Reservation> returnedReservations = reservationService.retrieveAllUpcoming(uuid);
        assertEquals(incorrectReservationString, reservation3, returnedReservations.get(0));
        assertEquals(incorrectReservationString, reservation2, returnedReservations.get(1));
        assertEquals(incorrectReservationString, reservation, returnedReservations.get(2));

        verify(reservationRepository, times(1)).findAllByTeamUUIDAndStartTimeAfter(eq(uuid), any());
    }

    @Test
    public void retrieveAllReservationsForTeam_ReturnsReservationsInCorrectOrder() {
        List<Reservation> reservations = new ArrayList<>();

        reservations.add(reservation2);
        reservations.add(reservation3);
        reservations.add(reservation);

        when(reservationRepository.findAllByTeamUUID(uuid))
                .thenReturn(reservations);

        List<Reservation> returnedReservations =
                reservationService.retrieveAllReservationsForTeam(uuid);

        assertEquals(incorrectReservationString, reservation3, returnedReservations.get(0));
        assertEquals(incorrectReservationString, reservation2, returnedReservations.get(1));
        assertEquals(incorrectReservationString, reservation, returnedReservations.get(2));

        verify(reservationRepository, times(1)).findAllByTeamUUID(uuid);
    }
}
