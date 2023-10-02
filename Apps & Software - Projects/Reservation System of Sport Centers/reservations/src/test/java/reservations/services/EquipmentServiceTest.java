package reservations.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import reservations.repositories.EquipmentRepository;
import reservations.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {
    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UsersClient usersClient;

    @Autowired
    @InjectMocks
    private EquipmentService equipmentService;

    UUID testEquipmentUuid = UUID.fromString("abcdef12-1234-5678-90ab-0123456789ab");
    UUID reservationTeamUuid = UUID.fromString("12345678-abcd-ef12-3456-0123456789ab");
    UUID otherTeamUuid = UUID.fromString("12345678-1234-1234-1234-0123456789ab");
    String testEquipmentName = "test name";
    String testSport = "test sport";

    int testCapacity = 10;
    int millisecondsInHour = 3_600_000;

    Equipment equipment = new Equipment(testEquipmentName, testCapacity, testSport);

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
    public void addEquipment_SuccessfullySaves() {
        equipmentService.addEquipment(testEquipmentName, testCapacity, testSport);
        verify(equipmentRepository, times(1)).save(any());
    }

    @Test
    public void deleteEquipment_SuccessfullyDeletes_WhenSuchEquipmentExists() {
        UUID id = equipment.getUuid();

        Reservation reservation1 =
                new Reservation(reservationTeamUuid, equipment, null, testCapacity);

        Reservation reservation2 =
                new Reservation(reservationTeamUuid, equipment, null, 5);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(reservationRepository.findAllByBookable(equipment)).thenReturn(reservations);

        equipmentService.deleteEquipment(id);

        verify(reservationRepository, times(1)).findAllByBookable(equipment);
        verify(equipmentRepository, times(1)).delete(equipment);
        verify(reservationRepository, times(1)).deleteAll(reservations);
    }

    @Test
    public void deleteEquipment_ThrowsException_WhenNoSuchEquipmentExists() {
        when(equipmentRepository.findById(testEquipmentUuid)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.deleteEquipment(testEquipmentUuid)
        );

        verify(reservationRepository, never()).findAllByBookable(equipment);
        verify(equipmentRepository, never()).delete(equipment);
        verify(reservationRepository, never()).deleteAll(any());
    }

    @Test
    public void updateEquipment_SetsCorrectParameters_WhenTheyAreValid() {
        UUID id = equipment.getUuid();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        String newName = "new name";
        int newCapacity = 43;
        String newRelatedSport = "new sport";

        equipmentService.updateEquipment(id, newName, newCapacity, newRelatedSport);

        assertEquals("Name has not been changed", newName, equipment.getName());
        assertEquals("Max capacity has not been changed", newCapacity, equipment.getMaxCapacity());
        assertEquals(
                "Related sport has not been changed",
                newRelatedSport,
                equipment.getRelatedSport()
        );

        assertEquals("UUID should not have changed", id, equipment.getUuid());
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    public void updateEquipment_ThrowsException_WhenEquipmentIsNotExistent() {
        UUID id = equipment.getUuid();
        when(equipmentRepository.findById(id)).thenReturn(Optional.empty());

        String newName = "new name";
        int newCapacity = 43;
        String newRelatedSport = "new sport";

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.updateEquipment(id, newName, newCapacity, newRelatedSport)
        );

        assertNotEquals("Name has been changed", newName, equipment.getName());
        assertNotEquals("Max capacity has been changed", newCapacity, equipment.getMaxCapacity());
        assertNotEquals(
                "Related sport has been changed",
                newRelatedSport,
                equipment.getRelatedSport()
        );

        assertEquals("UUID should not have changed", id, equipment.getUuid());
        verify(equipmentRepository, never()).save(equipment);
    }

    @Test
    public void getEquipment_ReturnsCorrectEntity_WhenValuesAreValid() {
        UUID id = equipment.getUuid();

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        Equipment returnedEquipment = equipmentService.getEquipment(id);

        assertEquals(
                "Different equipment than the one requested is returned",
                equipment,
                returnedEquipment
        );

        verify(equipmentRepository, times(1)).findById(id);
    }

    @Test
    public void getEquipment_ThrowsException_WhenNoSuchEquipmentExists() {
        when(equipmentRepository.findById(testEquipmentUuid)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.getEquipment(testEquipmentUuid)
        );

        verify(equipmentRepository, times(1)).findById(testEquipmentUuid);
    }

    @Test
    public void bookEquipment_SuccessfullyBooks_WhenAllParametersAreValid() {
        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        // Make the time of the reservation tomorrow at 6 o'clock, so it is a valid time.
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());

        int amountToBook = 5;

        Reservation reservation1 = new Reservation(reservationTeamUuid, equipment, null, 2);
        Reservation reservation2 = new Reservation(otherTeamUuid, equipment, null, 1);
        Reservation reservation3 = new Reservation(otherTeamUuid, equipment, null, 1);
        Reservation reservation4 = new Reservation(otherTeamUuid, equipment, null, 1);

        List<Reservation> reservations =
                List.of(reservation1, reservation2, reservation3, reservation4);

        when(equipmentRepository.findById(equipment.getUuid()))
                .thenReturn(Optional.of(equipment));

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                equipment, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        equipmentService.bookEquipment(
                reservationTeamUuid,
                equipment.getUuid(),
                reservationTimeString,
                amountToBook
        );

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(equipment, overlappingStart, overlappingEnd);
    }

    @Test
    public void bookEquipment_FailsToBook_WhenAmountGoesOverCapacity() {
        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));
        // Make the time of the reservation tomorrow at 6 o'clock, so it is a valid time.
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());

        int amountToBook = 10;

        Reservation reservation1 = new Reservation(reservationTeamUuid, equipment, null, 2);
        Reservation reservation2 = new Reservation(otherTeamUuid, equipment, null, 1);
        Reservation reservation3 = new Reservation(otherTeamUuid, equipment, null, 1);
        Reservation reservation4 = new Reservation(otherTeamUuid, equipment, null, 1);

        List<Reservation> reservations = List.of(
                reservation1,
                reservation2,
                reservation3,
                reservation4
        );

        when(equipmentRepository.findById(equipment.getUuid()))
                .thenReturn(Optional.of(equipment));

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                equipment, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        assertThrows(InvalidParameterException.class, () -> equipmentService.bookEquipment(
                reservationTeamUuid, equipment.getUuid(), reservationTimeString, amountToBook
        ));

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(
                equipment,
                overlappingStart,
                overlappingEnd
        );

        verify(reservationRepository, never()).save(any());
    }

    @Test
    public void bookEquipment_FailsToBook_WhenTeamDoesNotExist() {
        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(false)));

        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String reservationTimeString = dateFormat.format(tomorrow.getTime());

        int amountToBook = 10;

        assertThrows(InvalidParameterException.class, () -> equipmentService.bookEquipment(
                reservationTeamUuid, equipment.getUuid(), reservationTimeString, amountToBook
        ));

        verify(reservationRepository, never()).save(any());
    }

    @Test
    public void getAllEquipment_BehavesCorrectly() {
        Equipment equipment2 = new Equipment("bat", 2, "baseball");

        List<Equipment> equipments = new ArrayList<>();
        equipments.add(equipment);
        equipments.add(equipment2);

        when(equipmentRepository.findAll()).thenReturn(equipments);

        List<Equipment> returnedEquipment = equipmentService.getAllEquipment();

        assertEquals("Result should have been of different size", 2, returnedEquipment.size());
        verify(equipmentRepository, times(1)).findAll();
    }

    @Test
    public void getAllAvailableEquipment_ReturnsEmptyList_WhenNoEquipmentIsAvailable() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        Equipment baseballBat = new Equipment("baseball bat", 5, "baseball");

        List<Equipment> allEquipment = List.of(equipment, baseballBat);

        Reservation reservationForTest =
                new Reservation(reservationTeamUuid, equipment, null, testCapacity);

        Reservation reservationForBaseballBat =
                new Reservation(reservationTeamUuid, baseballBat, null, 5);

        List<Reservation> reservationsWithinPeriod =
                List.of(reservationForTest, reservationForBaseballBat);

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByStartTimeBetween(overlappingStart, overlappingEnd))
                .thenReturn(reservationsWithinPeriod);

        when(equipmentRepository.findAll()).thenReturn(allEquipment);

        List<Equipment> returnedEquipment = equipmentService.getAllAvailableEquipment(time);
        assertEquals("There should be no equipment available.", 0, returnedEquipment.size());
    }

    @Test
    public void getAllAvailableEquipment_ReturnsEmptyList_WhenNoEquipmentExists() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        List<Equipment> allEquipment = List.of();

        SportsFacility hall = new SportsFacility("Basketball hall", 30);

        Reservation reservationForTest = new Reservation(reservationTeamUuid, hall, null, 1);
        List<Reservation> reservationsWithinPeriod = List.of(reservationForTest);

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByStartTimeBetween(overlappingStart, overlappingEnd))
                .thenReturn(reservationsWithinPeriod);

        when(equipmentRepository.findAll()).thenReturn(allEquipment);

        List<Equipment> returnedEquipment = equipmentService.getAllAvailableEquipment(time);
        assertEquals("There should be equipment available.", 0, returnedEquipment.size());
    }

    @Test
    public void getAllAvailableSportsEquipment_ThrowsException_WhenTimestampInThePast() {
        tomorrow.add(Calendar.DATE, -2);
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.getAllAvailableEquipment(time)
        );
    }

    @Test
    public void getAllAvailableEquipment_ThrowsException_WhenTimestampNotInBounds() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.getAllAvailableEquipment(time)
        );
    }
    
    @Test
    public void getAllAvailableEquipment_ReturnsCorrectList_WhenSomeEquipmentIsAvailable() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        Equipment baseballBat = new Equipment("baseball bat", 5, "baseball");
        SportsFacility facility = new SportsFacility("facility", 75);

        List<Equipment> allEquipment = List.of(equipment, baseballBat);

        Reservation reservationForTest1 =
                new Reservation(reservationTeamUuid, equipment, null, 3);

        Reservation reservationForTest2 =
                new Reservation(reservationTeamUuid, equipment, null, 2);

        Reservation reservationForBaseballBat =
                new Reservation(reservationTeamUuid, baseballBat, null, 5);

        Reservation reservationForSportsHall =
                new Reservation(reservationTeamUuid, facility, null, 1);

        List<Reservation> reservationsWithinPeriod = List.of(
                        reservationForTest1,
                        reservationForTest2,
                        reservationForBaseballBat,
                        reservationForSportsHall
                );

        long tomorrowMilliseconds = tomorrow.getTime().getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByStartTimeBetween(overlappingStart, overlappingEnd))
                .thenReturn(reservationsWithinPeriod);

        when(equipmentRepository.findAll()).thenReturn(allEquipment);

        List<Equipment> returnedEquipment = equipmentService.getAllAvailableEquipment(time);
        assertEquals(
                "There should have been 1 piece of equipment available.",
                1,
                returnedEquipment.size()
        );

        assertTrue(
                "List should not contain the sports hall",
                !returnedEquipment.contains(facility)
        );

        assertTrue(
                "List should not contain the baseball bat",
                !returnedEquipment.contains(baseballBat)
        );

        assertTrue(
                "List should contain the test equipment",
                returnedEquipment.contains(equipment)
        );

        assertEquals(
                "Capacity of test equipment should have been 5",
                5,
                returnedEquipment.get(0).getMaxCapacity()
        );
    }

    @Test
    public void removeEquipmentBooking_DeletesCorrectBooking_WhenParametersValid() {
        when(reservationRepository.findById(reservationUuid))
                .thenReturn(Optional.of(reservationToRemove));

        equipmentService.removeEquipmentBooking(reservationUuid, reservationTeamUuid);
        verify(reservationRepository, times(1)).delete(reservationToRemove);
    }

    @Test
    public void removeEquipmentBooking_ThrowsException_WhenReservationDoesNotExist() {
        when(reservationRepository.findById(reservationUuid)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.removeEquipmentBooking(reservationUuid, reservationTeamUuid)
        );
    }

    @Test
    public void removeEquipmentBooking_ThrowsException_WhenADifferentTeamTriesToDelete() {
        when(reservationRepository.findById(reservationUuid))
                .thenReturn(Optional.of(reservationToRemove));

        assertThrows(
                InvalidParameterException.class,
                () -> equipmentService.removeEquipmentBooking(reservationUuid, otherTeamUuid)
        );
    }
}
