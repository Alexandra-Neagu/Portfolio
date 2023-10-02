package reservations.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.entities.Equipment;
import reservations.entities.Reservation;
import reservations.entities.SportsFacility;
import reservations.repositories.ReservationRepository;
import reservations.repositories.SportsFacilityRepository;

@ExtendWith(MockitoExtension.class)
public class SportsFacilityServiceTest {
    @Mock
    private SportsFacilityRepository sportsFacilityRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UsersClient usersClient;

    @Autowired
    @InjectMocks
    private SportsFacilityService sportsFacilityService;

    UUID testSportsFacilityUuid = UUID.fromString("abcdef12-1234-5678-90ab-0123456789ab");
    UUID reservationTeamUuid = UUID.fromString("12345678-abcd-ef12-3456-0123456789ab");
    UUID otherTeamUuid = UUID.fromString("12345678-1234-1234-1234-0123456789ab");
    String testSportsFacilityName = "test name";
    String testSport = "test sport";

    int testMinCapacity = 2;
    int testMaxCapacity = 4;
    int millisecondsInHour = 3_600_000;

    SportsFacility sportsFacility =
            new SportsFacility(testSportsFacilityName, testMinCapacity, testMaxCapacity);

    Calendar tomorrow;

    SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.GERMANY);

    Reservation reservationToRemove = new Reservation(reservationTeamUuid, null, null);
    UUID reservationUuid = reservationToRemove.getReservationUUID();

    /**
     * Instantiates some of the objects that are used in the test before each test
     * is executed.
     */
    @BeforeEach
    public void setup() {
        tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
    }

    @Test
    public void addSportsFacility_SuccessfullySaves() {
        sportsFacilityService.addSportsFacility(
                testSportsFacilityName,
                testMinCapacity,
                testMaxCapacity
        );

        verify(sportsFacilityRepository, times(1)).save(any());
    }

    @Test
    public void deleteSportsFacility_SuccessfullyDeletes_WhenSuchFacilityExists() {
        UUID id = sportsFacility.getUuid();
        when(sportsFacilityRepository.findById(id)).thenReturn(Optional.of(sportsFacility));

        Reservation reservation1 = new Reservation(reservationTeamUuid, sportsFacility, null);
        Reservation reservation2 = new Reservation(otherTeamUuid, sportsFacility, null);
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(reservationRepository.findAllByBookable(sportsFacility))
                .thenReturn(reservations);

        sportsFacilityService.deleteSportsFacility(id);

        verify(sportsFacilityRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).findAllByBookable(sportsFacility);
        verify(sportsFacilityRepository, times(1)).delete(sportsFacility);
        verify(reservationRepository, times(1)).deleteAll(reservations);
        verify(usersClient, times(1))
                .deleteReservationForTeam(reservation1.getReservationUUID(), reservationTeamUuid);
        verify(usersClient, times(1))
                .deleteReservationForTeam(reservation2.getReservationUUID(), otherTeamUuid);
    }

    @Test
    public void deleteSportsFacility_ThrowsException_WhenNoSuchFacilityExists() {
        UUID id = sportsFacility.getUuid();
        when(sportsFacilityRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.deleteSportsFacility(id)
        );

        verify(sportsFacilityRepository, times(1)).findById(id);
        verify(reservationRepository, never()).findAllByBookable(any());
        verify(reservationRepository, never()).deleteAll(any());
        verify(sportsFacilityRepository, never()).delete(any());
        verify(usersClient, never()).deleteReservationForTeam(any(), any());
    }

    @Test
    public void updateEquipment_SetsCorrectParameters_WhenTheyAreValid() {
        UUID id = sportsFacility.getUuid();
        when(sportsFacilityRepository.findById(id)).thenReturn(Optional.of(sportsFacility));

        String newName = "new name";
        int newMinCapacity = 3;
        int newMaxCapacity = 32;

        sportsFacilityService.updateSportsFacility(id, newName, newMinCapacity, newMaxCapacity);

        assertEquals("Name has not been changed", newName, sportsFacility.getName());
        assertEquals(
                "Min capacity has not been changed",
                newMinCapacity,
                sportsFacility.getMinCapacity()
        );
        assertEquals(
                "Max capacity has not been changed",
                newMaxCapacity,
                sportsFacility.getMaxCapacity()
        );

        assertEquals("UUID should not have changed", id, sportsFacility.getUuid());
        verify(sportsFacilityRepository, times(1)).save(sportsFacility);
    }

    @Test
    public void updateEquipment_ThrowsException_WhenEquipmentIsNotExistent() {
        UUID id = sportsFacility.getUuid();
        when(sportsFacilityRepository.findById(id)).thenReturn(Optional.empty());

        String newName = "new name";
        int newMinCapacity = 3;
        int newMaxCapacity = 32;

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService
                        .updateSportsFacility(id, newName, newMinCapacity, newMaxCapacity)
        );

        assertNotEquals("Name has been changed", newName, sportsFacility.getName());
        assertNotEquals(
                "Min capacity has been changed",
                newMinCapacity,
                sportsFacility.getMinCapacity()
        );
        assertNotEquals(
                "Max capacity has been changed",
                newMaxCapacity,
                sportsFacility.getMaxCapacity()
        );

        assertEquals("UUID should not have changed", id, sportsFacility.getUuid());
        verify(sportsFacilityRepository, never()).save(sportsFacility);
    }


    @Test
    public void getSportsFacility_ReturnsCorrectEntity_WhenValuesAreValid() {
        UUID id = sportsFacility.getUuid();

        when(sportsFacilityRepository.findById(id)).thenReturn(Optional.of(sportsFacility));

        SportsFacility returnedSportsFacility = sportsFacilityService.getSportsFacility(id);

        assertEquals(
                "Different sports facility than the one requested is returned",
                sportsFacility,
                returnedSportsFacility
        );

        verify(sportsFacilityRepository, times(1)).findById(id);
    }

    @Test
    public void getSportsFacility_ThrowsException_WhenNoSuchSportsFacilityExists() {
        when(sportsFacilityRepository.findById(testSportsFacilityUuid))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.getSportsFacility(testSportsFacilityUuid)
        );

        verify(sportsFacilityRepository, times(1)).findById(testSportsFacilityUuid);
    }

    @Test
    public void bookSportsFacility_SuccessfullyBooks_WhenAllParametersAreValid() {
        bookSportsFacility_ShouldSucceed(3);
    }

    @Test
    public void bookSportsFacility_FailsToBook_WhenFacilityAlreadyBooked() {
        // Make the time of the reservation tomorrow at 6 o'clock, so it is a valid time.
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());

        Reservation reservation = new Reservation(otherTeamUuid, sportsFacility, null, 1);

        List<Reservation> reservations = List.of(reservation);

        when(sportsFacilityRepository.findById(sportsFacility.getUuid()))
                .thenReturn(Optional.of(sportsFacility));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(reservationTeamUuid, reservationTimeString))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(3)));

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                sportsFacility, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.bookSportsFacility(
                        reservationTeamUuid,
                        sportsFacility.getUuid(),
                        reservationTimeString
                )
        );

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(
                sportsFacility,
                overlappingStart,
                overlappingEnd
        );

        verify(reservationRepository, never()).save(any());
    }

    @Test
    public void bookSportsFacility_FailsToBook_WhenTeamIsOfSmallerSize() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());
        when(sportsFacilityRepository.findById(sportsFacility.getUuid()))
                .thenReturn(Optional.of(sportsFacility));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(reservationTeamUuid, reservationTimeString))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(1)));

        assertThrows(InvalidParameterException.class,
                () -> sportsFacilityService.bookSportsFacility(
                        reservationTeamUuid,
                        sportsFacility.getUuid(),
                        reservationTimeString
                )
        );
    }

    private void bookSportsFacility_ShouldSucceed(int teamSize) {
        // Make the time of the reservation tomorrow at 6 o'clock, so it is a valid time.
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());

        List<Reservation> reservations = List.of();

        when(sportsFacilityRepository.findById(sportsFacility.getUuid()))
                .thenReturn(Optional.of(sportsFacility));

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                sportsFacility, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(reservationTeamUuid, reservationTimeString))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(teamSize)));


        sportsFacilityService.bookSportsFacility(
                reservationTeamUuid,
                sportsFacility.getUuid(),
                reservationTimeString
        );

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(sportsFacility, overlappingStart, overlappingEnd);

        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    public void bookSportsFacility_FailsToBook_WhenSuchTeamDoesNotExist() {
        String reservationTimeString = dateFormat.format(tomorrow.getTime());
        when(sportsFacilityRepository.findById(sportsFacility.getUuid()))
                .thenReturn(Optional.of(sportsFacility));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(InvalidParameterException.class,
                () -> sportsFacilityService.bookSportsFacility(
                        reservationTeamUuid,
                        sportsFacility.getUuid(),
                        reservationTimeString
                )
        );
    }

    @Test
    public void bookSportsFacility_FailsToBook_WhenTeamCannotBook() {
        String reservationTimeString = dateFormat.format(tomorrow.getTime());
        when(sportsFacilityRepository.findById(sportsFacility.getUuid()))
                .thenReturn(Optional.of(sportsFacility));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(reservationTeamUuid, reservationTimeString))
                .thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(InvalidParameterException.class,
                () -> sportsFacilityService.bookSportsFacility(
                        reservationTeamUuid,
                        sportsFacility.getUuid(),
                        reservationTimeString
                )
        );
    }

    @Test
    public void bookSportsFacility_Succeeds_WhenTeamSizeIsOnLowerBoundary() {
        bookSportsFacility_ShouldSucceed(testMinCapacity);
    }

    @Test
    public void bookSportsFacility_Succeeds_WhenTeamSizeIsOnUpperBoundary() {
        bookSportsFacility_ShouldSucceed(testMaxCapacity);
    }

    @Test
    public void bookSportsFacility_FailsToBook_WhenTeamIsOfBiggerSize() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());
        when(sportsFacilityRepository.findById(sportsFacility.getUuid()))
                .thenReturn(Optional.of(sportsFacility));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(reservationTeamUuid, reservationTimeString))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(5)));

        assertThrows(InvalidParameterException.class,
                () -> sportsFacilityService.bookSportsFacility(
                        reservationTeamUuid,
                        sportsFacility.getUuid(),
                        reservationTimeString
                )
        );
    }

    @Test
    public void getAllSportsFacilities_BehavesCorrectly() {
        SportsFacility sportsFacility2 = new SportsFacility("hall1", 2, 3);

        List<SportsFacility> sportsFacilities = List.of(sportsFacility, sportsFacility2);
        when(sportsFacilityRepository.findAll()).thenReturn(sportsFacilities);

        List<SportsFacility> returnedSportsFacility =
                sportsFacilityService.getAllSportsFacilities();

        assertEquals("Result should have been of different size", 2, returnedSportsFacility.size());
        verify(sportsFacilityRepository, times(1)).findAll();
    }

    @Test
    public void
        getAllAvailableSportsFacilities_ReturnsEmptyList_WhenNoSportsFacilityIsAvailable() {

        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        SportsFacility sportsFacility2 = new SportsFacility("hall1", 2, 3);

        List<SportsFacility> allSportsFacilities = List.of(sportsFacility, sportsFacility2);

        Reservation reservationForFacility1 =
                new Reservation(reservationTeamUuid, sportsFacility, null, 1);

        Reservation reservationForFacility2 =
                new Reservation(reservationTeamUuid, sportsFacility2, null, 1);

        List<Reservation> reservationsWithinPeriod =
                List.of(reservationForFacility1, reservationForFacility2);

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByStartTimeBetween(overlappingStart, overlappingEnd))
                .thenReturn(reservationsWithinPeriod);

        when(sportsFacilityRepository.findAll()).thenReturn(allSportsFacilities);

        List<SportsFacility> returnedSportsFacilities =
                sportsFacilityService.getAllAvailableSportsFacilities(time);

        assertEquals(
                "There should be no facilities available.",
                0,
                returnedSportsFacilities.size()
        );
    }

    @Test
    public void getAllAvailableSportsFacilities_ReturnsEmptyList_WhenNoSportsFacilitiesExist() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        List<SportsFacility> allSportsFacilities = List.of();

        Equipment equipment = new Equipment("Basketball ball", 3, "basketball");

        Reservation reservationForEquipment =
                new Reservation(reservationTeamUuid, equipment, null, 1);
        List<Reservation> reservationsWithinPeriod = List.of(reservationForEquipment);

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByStartTimeBetween(overlappingStart, overlappingEnd))
                .thenReturn(reservationsWithinPeriod);

        when(sportsFacilityRepository.findAll()).thenReturn(allSportsFacilities);

        List<SportsFacility> returnedSportsFacilities =
                sportsFacilityService.getAllAvailableSportsFacilities(time);

        assertEquals(
                "There should be no facilities available.",
                0,
                returnedSportsFacilities.size()
        );
    }

    @Test
    public void getAllAvailableSportsFacilities_ThrowsException_WhenTimestampInThePast() {
        tomorrow.add(Calendar.DATE, -2);
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.getAllAvailableSportsFacilities(time)
        );
    }

    @Test
    public void getAllAvailableSportsFacilities_ThrowsException_WhenTimestampNotInBounds() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.getAllAvailableSportsFacilities(time)
        );
    }

    @Test
    public void getAllAvailableSportsFacilities_ReturnsCorrectList_WhenSomeAreAvailable() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        SportsFacility sportsFacility2 = new SportsFacility("hall1", 2, 3);
        SportsFacility sportsFacility3 = new SportsFacility("facility", 75);
        Equipment equipment = new Equipment("equipment", 10, "test sport");

        List<SportsFacility> allSportsFacilities =
                List.of(sportsFacility, sportsFacility2, sportsFacility3);

        Reservation reservationForFacility1 =
                new Reservation(reservationTeamUuid, sportsFacility, null, 1);

        Reservation reservationForEquipment =
                new Reservation(reservationTeamUuid, equipment, null, 1);

        List<Reservation> reservationsWithinPeriod = List.of(
                reservationForFacility1,
                reservationForEquipment
        );

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByStartTimeBetween(overlappingStart, overlappingEnd))
                .thenReturn(reservationsWithinPeriod);

        when(sportsFacilityRepository.findAll()).thenReturn(allSportsFacilities);

        List<SportsFacility> returnedSportsFacility =
                sportsFacilityService.getAllAvailableSportsFacilities(time);

        assertEquals(
                "There should have been 2 sports facilities available.",
                2,
                returnedSportsFacility.size()
        );

        assertTrue(
                "List should not contain the equipment",
                !returnedSportsFacility.contains(equipment)
        );

        assertTrue(
                "List should not contain the first sports facility",
                !returnedSportsFacility.contains(sportsFacility)
        );

        assertTrue(
                "List should contain the second sports facility",
                returnedSportsFacility.contains(sportsFacility2)
        );

        assertTrue(
                "List should contain the third sports facility",
                returnedSportsFacility.contains(sportsFacility3)
        );
    }

    @Test
    public void removeSportsFacilityBooking_DeletesCorrectBooking_WhenParametersValid() {
        when(reservationRepository.findById(reservationUuid))
                .thenReturn(Optional.of(reservationToRemove));

        sportsFacilityService.removeSportsFacilityBooking(reservationUuid, reservationTeamUuid);
        verify(reservationRepository, times(1)).delete(reservationToRemove);
        verify(usersClient, times(1))
                .deleteReservationForTeam(reservationUuid, reservationTeamUuid);
    }

    @Test
    public void removeSportsFacilityBooking_ThrowsException_WhenReservationDoesNotExist() {
        when(reservationRepository.findById(reservationUuid)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.removeSportsFacilityBooking(
                        reservationUuid,
                        reservationTeamUuid
                )
        );
        verify(usersClient, never())
                .deleteReservationForTeam(reservationUuid, reservationTeamUuid);
    }

    @Test
    public void removeSportsFacilityBooking_ThrowsException_WhenADifferentTeamTriesToDelete() {
        when(reservationRepository.findById(reservationUuid))
                .thenReturn(Optional.of(reservationToRemove));

        assertThrows(
                InvalidParameterException.class,
                () -> sportsFacilityService.removeSportsFacilityBooking(
                        reservationUuid,
                        otherTeamUuid
                )
        );

        verify(usersClient, never())
                .deleteReservationForTeam(reservationUuid, reservationTeamUuid);
    }
}
