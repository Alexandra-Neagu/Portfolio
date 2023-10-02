package reservations.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import reservations.entities.Lesson;
import reservations.entities.Reservation;
import reservations.entities.SportsFacility;
import reservations.repositories.LessonRepository;
import reservations.repositories.ReservationRepository;
import reservations.repositories.SportsFacilityRepository;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {
    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private SportsFacilityRepository sportsFacilityRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UsersClient usersClient;

    @Autowired
    @InjectMocks
    private LessonService lessonService;

    UUID testLessonUuid = UUID.fromString("abcdef12-1234-5678-90ab-0123456789ab");
    UUID reservationTeamUuid = UUID.fromString("12345678-abcd-ef12-3456-0123456789ab");
    UUID otherTeamUuid = UUID.fromString("12345678-1234-1234-1234-0123456789ab");

    String testSportsFacilityName = "test name";
    String testLessonName = "lesson name";

    int millisecondsInHour = 3_600_000;

    String testStartTime = "2021-12-30T15:22:35.016+02:00";
    String testEndTime = "2021-12-30T16:00:00.000+02:00";
    Timestamp testStartTimeTimestamp = parseStringToTimestamp(testStartTime);
    Timestamp testEndTimeTimestamp = parseStringToTimestamp(testEndTime);

    SportsFacility testSportsFacility =
            new SportsFacility(testSportsFacilityName, 2, 4);

    Lesson lesson = new Lesson(
            "lesson1",
            5,
            null,
            testStartTimeTimestamp,
            testEndTimeTimestamp
    );

    Lesson lesson2 = new Lesson(
            "lesson2",
            3,
            testSportsFacility,
            testStartTimeTimestamp,
            testEndTimeTimestamp
    );

    Calendar tomorrow;
    Calendar tomorrowButTwoHoursLater;

    SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.GERMANY);

    Reservation reservationToRemove = new Reservation(reservationTeamUuid, null, null);
    UUID reservationUuid = reservationToRemove.getReservationUUID();

    String newName = "new name";

    /**
     * Instantiates some of the objects that are used in the test before each test
     * is executed.
     */
    @BeforeEach
    public void setup() {
        tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        tomorrowButTwoHoursLater = Calendar.getInstance();
        tomorrowButTwoHoursLater.add(Calendar.DATE, 1);
    }

    @Test
    public void addLesson_SuccessfullySaves() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 9);
        String startTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 11);
        String endTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        UUID facilityId = testSportsFacility.getUuid();
        UUID lessonId = lesson.getUuid();
        List<Lesson> lessons = List.of();

        when(sportsFacilityRepository.findById(facilityId))
                .thenReturn(Optional.of(testSportsFacility));
        when(lessonRepository.findDistinctBySportsFacilityAndStartTimeBetween(
                testSportsFacility,
                parseStringToTimestamp(startTime),
                parseStringToTimestamp(endTime)
        )).thenReturn(lessons);

        lessonService.addLesson(
                testLessonName,
                4,
                testSportsFacility.getUuid(),
                startTime,
                endTime
        );

        verify(lessonRepository, times(1)).save(any());
    }

    @Test
    public void addLesson_ThrowsException_WhenTimestampInThePast() {
        when(sportsFacilityRepository.findById(testSportsFacility.getUuid()))
                .thenReturn(Optional.of(testSportsFacility));

        tomorrow.add(Calendar.DATE, -2);
        tomorrow.set(Calendar.HOUR_OF_DAY, 11);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.addLesson(
                        testLessonName,
                        lesson.getMaxCapacity(),
                        testSportsFacility.getUuid(),
                        time,
                        time
                )
        );
    }

    @Test
    public void addLesson_ThrowsException_WhenTimestampNotInBounds() {
        when(sportsFacilityRepository.findById(testSportsFacility.getUuid()))
                .thenReturn(Optional.of(testSportsFacility));

        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.addLesson(
                        testLessonName,
                        lesson.getMaxCapacity(),
                        testSportsFacility.getUuid(),
                        time,
                        time
                )
        );
    }

    @Test
    public void addLesson_ThrowsException_WhenStartTimeGreaterThanEndTime() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 11);
        String startTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 9);
        String endTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        when(sportsFacilityRepository.findById(testSportsFacility.getUuid()))
                .thenReturn(Optional.of(testSportsFacility));

        assertThrows(
                InvalidParameterException.class,
                () ->   lessonService.addLesson(
                        testLessonName,
                        4,
                        testSportsFacility.getUuid(),
                        startTime,
                        endTime
                )
        );

        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void addLesson_FailsDueToExistingLessonInTimeslot() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 9);
        String startTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 11);
        String endTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        UUID facilityId = testSportsFacility.getUuid();

        List<Lesson> lessons = List.of(lesson);

        when(sportsFacilityRepository.findById(facilityId))
                .thenReturn(Optional.of(testSportsFacility));

        when(lessonRepository.findDistinctBySportsFacilityAndStartTimeBetween(
                testSportsFacility,
                parseStringToTimestamp(startTime),
                parseStringToTimestamp(endTime)
        )).thenReturn(lessons);

        assertThrows(
            InvalidParameterException.class,
            () ->   lessonService.addLesson(
                        testLessonName,
                        4,
                        testSportsFacility.getUuid(),
                        startTime,
                        endTime
                    )
        );

        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_ThrowsException_WhenLessonDoesNotExist() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();
        String newStartTime = "2021-12-31T14:32:37.016+02:00";
        String newEndTime = "2021-12-31T15:20:00.450+02:00";

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.updateLesson(
                        id,
                        newName,
                        newCapacity,
                        newSportsFacilityUuid,
                        newStartTime,
                        newEndTime
                )
        );

        verify(lessonRepository, times(1)).findById(id);
        verify(sportsFacilityRepository, never()).findById(newSportsFacilityUuid);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_ThrowsException_WhenTimestampCannotBeParsed() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();
        String newStartTime = "non-parsable timestamp";

        when(sportsFacilityRepository.findById(newSportsFacilityUuid))
                .thenReturn(Optional.empty());

        assertThrows(
            InvalidParameterException.class,
            () -> lessonService.updateLesson(
                    id,
                    newName,
                    newCapacity,
                    newSportsFacilityUuid,
                    newStartTime,
                    newStartTime
            )
        );

        verify(lessonRepository, times(1)).findById(id);
        verify(sportsFacilityRepository, times(1)).findById(newSportsFacilityUuid);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_ThrowsException_WhenTimestampInThePast() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();
        String newStartTime = "2021-12-31T14:32:37.016+02:00";
        String newEndTime = "2021-12-31T15:20:00.450+02:00";

        when(sportsFacilityRepository.findById(newSportsFacilityUuid))
                .thenReturn(Optional.of(testSportsFacility));

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.updateLesson(
                        id,
                        newName,
                        newCapacity,
                        newSportsFacilityUuid,
                        newStartTime,
                        newEndTime
                )
        );

        verify(sportsFacilityRepository, times(1)).findById(newSportsFacilityUuid);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_ThrowsException_WhenTimestampNotInBounds() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        tomorrow.set(Calendar.HOUR_OF_DAY, 18);
        String newStartTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 20);
        String newEndTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();

        when(sportsFacilityRepository.findById(newSportsFacilityUuid))
                .thenReturn(Optional.of(testSportsFacility));

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.updateLesson(
                        id,
                        newName,
                        newCapacity,
                        newSportsFacilityUuid,
                        newStartTime,
                        newEndTime
                )
        );

        verify(sportsFacilityRepository, times(1)).findById(newSportsFacilityUuid);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_ThrowsException_WhenEndTimeBeforeStartTime() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        tomorrow.set(Calendar.HOUR_OF_DAY, 12);
        String newStartTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 10);
        String newEndTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();

        when(sportsFacilityRepository.findById(newSportsFacilityUuid))
                .thenReturn(Optional.of(testSportsFacility));

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.updateLesson(
                        id,
                        newName,
                        newCapacity,
                        newSportsFacilityUuid,
                        newStartTime,
                        newEndTime
                )
        );

        verify(lessonRepository, never())
                .findDistinctBySportsFacilityAndStartTimeBetween(any(), any(), any());
        verify(sportsFacilityRepository, times(1)).findById(newSportsFacilityUuid);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_ThrowsException_WhenOtherLessonsInSameSportsFacility() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        String newStartTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 12);
        String newEndTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();

        List<Lesson> lessons = List.of(lesson);

        when(lessonRepository.findDistinctBySportsFacilityAndStartTimeBetween(
                testSportsFacility,
                parseStringToTimestamp(newStartTime),
                parseStringToTimestamp(newEndTime)
        )).thenReturn(lessons);

        when(sportsFacilityRepository.findById(newSportsFacilityUuid))
                .thenReturn(Optional.of(testSportsFacility));

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.updateLesson(
                        id,
                        newName,
                        newCapacity,
                        newSportsFacilityUuid,
                        newStartTime,
                        newEndTime
                )
        );

        verify(lessonRepository, times(1))
                .findDistinctBySportsFacilityAndStartTimeBetween(any(), any(), any());
        verify(sportsFacilityRepository, times(1)).findById(newSportsFacilityUuid);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLesson_SuccessfullySaves_WhenEverythingIsValid() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        String newStartTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 12);
        String newEndTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        int newCapacity = 45;
        UUID newSportsFacilityUuid = UUID.randomUUID();

        List<Lesson> lessons = List.of();

        when(lessonRepository.findDistinctBySportsFacilityAndStartTimeBetween(
                testSportsFacility,
                parseStringToTimestamp(newStartTime),
                parseStringToTimestamp(newEndTime)
        )).thenReturn(lessons);

        when(sportsFacilityRepository.findById(newSportsFacilityUuid))
                .thenReturn(Optional.of(testSportsFacility));

        lessonService.updateLesson(
                id,
                newName,
                newCapacity,
                newSportsFacilityUuid,
                newStartTime,
                newEndTime
        );

        assertEquals("UUID was changed", id, lesson.getUuid());
        assertEquals("Name does not match", newName, lesson.getName());
        assertEquals("Max capacity doesn't match", newCapacity, lesson.getMaxCapacity());
        assertEquals("Facility does not match", testSportsFacility, lesson.getSportsFacility());
        assertEquals(
                "Start time does not match",
                parseStringToTimestamp(newStartTime),
                lesson.getStartTime()
        );

        assertEquals(
                "End time does not match",
                parseStringToTimestamp(newEndTime),
                lesson.getEndTime()
        );

        verify(lessonRepository, times(1))
                .findDistinctBySportsFacilityAndStartTimeBetween(any(), any(), any());
        verify(sportsFacilityRepository, times(1)).findById(newSportsFacilityUuid);
        verify(lessonRepository, times(1)).save(any());
    }

    @Test
    public void deleteLesson_BehavesCorrectly() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        Reservation reservation1 = new Reservation(reservationTeamUuid, lesson, null);
        Reservation reservation2 = new Reservation(otherTeamUuid, lesson, null);
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(reservationRepository.findAllByBookable(lesson))
                .thenReturn(reservations);

        lessonService.deleteLesson(id);

        verify(lessonRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).findAllByBookable(lesson);
        verify(lessonRepository, times(1)).delete(lesson);
        verify(reservationRepository, times(1)).deleteAll(reservations);
        verify(usersClient, times(1))
                .deleteReservationForTeam(reservation1.getReservationUUID(), reservationTeamUuid);
        verify(usersClient, times(1))
                .deleteReservationForTeam(reservation2.getReservationUUID(), otherTeamUuid);
    }

    @Test
    public void deleteLesson_ThrowsException_WhenNoSuchLessonExists() {
        UUID id = lesson.getUuid();
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.deleteLesson(id)
        );

        verify(lessonRepository, times(1)).findById(id);
        verify(reservationRepository, never()).findAllByBookable(any());
        verify(reservationRepository, never()).deleteAll(any());
        verify(lessonRepository, never()).delete(any());
        verify(usersClient, never()).deleteReservationForTeam(any(), any());
    }

    @Test
    public void getLesson_ReturnsCorrectEntity_WhenValuesAreValid() {
        UUID id = lesson.getUuid();

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

        Lesson returnedLesson = lessonService.getLesson(id);

        assertEquals(
                "Different lesson than the one requested is returned",
                lesson,
                returnedLesson
        );

        verify(lessonRepository, times(1)).findById(id);
    }

    @Test
    public void getLesson_ThrowsException_WhenNoSuchLessonExists() {
        when(lessonRepository.findById(testLessonUuid))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.getLesson(testLessonUuid)
        );

        verify(lessonRepository, times(1)).findById(testLessonUuid);
    }

    @Test
    public void getSportsFacility_ReturnsCorrectEntity_WhenValuesAreValid() {
        when(sportsFacilityRepository.findById(testSportsFacility.getUuid()))
                .thenReturn(Optional.of(testSportsFacility));

        SportsFacility returnedSportsFacility =
                lessonService.getSportsFacility(testSportsFacility.getUuid());

        assertEquals(
                "Different sports facility than the one requested is returned",
                testSportsFacility,
                returnedSportsFacility
        );

        verify(sportsFacilityRepository, times(1))
                .findById(testSportsFacility.getUuid());
    }

    @Test
    public void getSportsFacility_ThrowsException_WhenNoSuchLessonExists() {
        when(sportsFacilityRepository.findById(testSportsFacility.getUuid()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.getSportsFacility(testSportsFacility.getUuid())
        );

        verify(sportsFacilityRepository, times(1))
                .findById(testSportsFacility.getUuid());
    }

    @Test
    public void bookLesson_SuccessfullyBooks_WhenNoReservationsForLesson() {
        List<Reservation> reservations = List.of();

        when(lessonRepository.findById(lesson.getUuid()))
                .thenReturn(Optional.of(lesson));

        when(reservationRepository.findAllByBookable(lesson)).thenReturn(reservations);

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(eq(reservationTeamUuid), any()))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(lesson.getMaxCapacity())));
        lessonService.bookLesson(reservationTeamUuid, lesson.getUuid());

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookable(lesson);

        verify(reservationRepository, times(1)).save(any());
        verify(usersClient, times(1))
                .addReservationForTeam(eq(reservationTeamUuid), any(), any());
    }

    @Test
    public void bookLesson_SuccessfullyBooks_WhenTeamSizeOnCapacityBoundary() {
        Reservation reservation1 = new Reservation(otherTeamUuid, lesson, null, 3);
        Reservation reservation2 = new Reservation(otherTeamUuid, lesson, null, 1);
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(lessonRepository.findById(lesson.getUuid()))
                .thenReturn(Optional.of(lesson));

        when(reservationRepository.findAllByBookable(lesson)).thenReturn(reservations);

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(eq(reservationTeamUuid), any()))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(1)));

        lessonService.bookLesson(reservationTeamUuid, lesson.getUuid());

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookable(lesson);

        verify(reservationRepository, times(1)).save(any());
        verify(usersClient, times(1))
                .addReservationForTeam(eq(reservationTeamUuid), any(), any());
    }

    @Test
    public void bookLesson_FailsToBook_WhenTeamSizeGoesOverBoundary() {
        Reservation reservation1 = new Reservation(otherTeamUuid, lesson, null, 3);
        Reservation reservation2 = new Reservation(otherTeamUuid, lesson, null, 2);
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(lessonRepository.findById(lesson.getUuid()))
                .thenReturn(Optional.of(lesson));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(eq(reservationTeamUuid), any()))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.getTeamSize(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(1)));

        when(reservationRepository.findAllByBookable(lesson)).thenReturn(reservations);

        assertThrows(InvalidParameterException.class,
                () -> lessonService.bookLesson(reservationTeamUuid, lesson.getUuid()));

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookable(lesson);

        verify(usersClient, never())
                .addReservationForTeam(eq(reservationTeamUuid), any(), any());
    }

    @Test
    public void bookLesson_FailsToBook_WhenTeamDoesNotExist() {
        when(lessonRepository.findById(lesson.getUuid()))
                .thenReturn(Optional.of(lesson));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(InvalidParameterException.class,
                () -> lessonService.bookLesson(reservationTeamUuid, lesson.getUuid()));

        verify(reservationRepository, never()).save(any());

        verify(usersClient, never())
                .addReservationForTeam(eq(reservationTeamUuid), any(), any());
    }

    @Test
    public void bookLesson_FailsToBook_WhenTeamCannotBookAnymore() {
        when(lessonRepository.findById(lesson.getUuid()))
                .thenReturn(Optional.of(lesson));

        when(usersClient.doesTeamExist(reservationTeamUuid))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        when(usersClient.canTeamBook(eq(reservationTeamUuid), any()))
                .thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(InvalidParameterException.class,
                () -> lessonService.bookLesson(reservationTeamUuid, lesson.getUuid()));

        verify(reservationRepository, never()).save(any());

        verify(usersClient, never())
                .addReservationForTeam(eq(reservationTeamUuid), any(), any());
    }

    @Test
    public void getAllLessons_BehavesCorrectly() {
        List<Lesson> lessons = List.of(lesson, lesson2);
        when(lessonRepository.findAll()).thenReturn(lessons);

        List<Lesson> returnedLesson =
                lessonService.getAllLessons();

        assertEquals("Result should have been of different size", 2, returnedLesson.size());
        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    public void getAllAvailableLessons_ReturnsEmptyList_WhenNoLessonIsAvailable() {
        Reservation reservationForLesson1 =
                new Reservation(reservationTeamUuid, lesson, null, 3);

        Reservation anotherReservationForLesson1 =
                new Reservation(reservationTeamUuid, lesson, null, 2);

        Reservation reservationForLesson2 =
                new Reservation(reservationTeamUuid, lesson2, null, 3);

        List<Reservation> reservationsForLesson1 = List.of(
                reservationForLesson1,
                anotherReservationForLesson1
        );

        List<Reservation> reservationsForLesson2 = List.of(
                reservationForLesson2
        );

        when(reservationRepository.findAllByBookable(lesson))
                .thenReturn(reservationsForLesson1);

        when(reservationRepository.findAllByBookable(lesson2))
                .thenReturn(reservationsForLesson2);

        tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        String startTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 12);
        String endTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        List<Lesson> allLessons = List.of(lesson, lesson2);

        when(lessonRepository.findAllByStartTimeAfterAndEndTimeBefore(
                parseStringToTimestamp(startTime),
                parseStringToTimestamp(endTime)
        )).thenReturn(allLessons);

        List<Lesson> returnedLessons =
                lessonService.getAllAvailableLessons(startTime, endTime);

        assertEquals(
                "There should be no lesson available.",
                0,
                returnedLessons.size()
        );
    }

    @Test
    public void getAllAvailableLessons_ReturnsEmptyList_WhenNoLessonsExist() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 9);
        String startTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 11);
        String endTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        List<Lesson> allLessons = List.of();

        when(lessonRepository.findAllByStartTimeAfterAndEndTimeBefore(
                parseStringToTimestamp(startTime),
                parseStringToTimestamp(endTime)
        )).thenReturn(allLessons);

        List<Lesson> returnedLessons =
                lessonService.getAllAvailableLessons(startTime, endTime);

        assertEquals(
                "There should be no lessons available.",
                0,
                returnedLessons.size()
        );
    }

    @Test
    public void getAllAvailableSportsEquipment_ThrowsException_WhenTimestampInThePast() {
        tomorrow.add(Calendar.DATE, -2);
        tomorrow.set(Calendar.HOUR_OF_DAY, 11);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.getAllAvailableLessons(time, time)
        );
    }

    @Test
    public void getAllAvailableEquipment_ThrowsException_WhenTimestampNotInBounds() {
        tomorrow.set(Calendar.HOUR_OF_DAY, 19);
        String time = dateFormat.format(tomorrow.getTime());

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.getAllAvailableLessons(time, time)
        );
    }

    @Test
    public void getAllAvailableLessons_ReturnsCorrectList_WhenSomeAreAvailable() {
        Lesson lesson3 =
                new Lesson(
                        "lesson3",
                        4,
                        testSportsFacility,
                        testStartTimeTimestamp,
                        testEndTimeTimestamp
                );

        Reservation reservationForLesson1 =
                new Reservation(reservationTeamUuid, lesson, null, 5);

        List<Reservation> reservationsForLesson1 = List.of(reservationForLesson1);
        List<Reservation> reservationsForLesson2 = List.of();
        List<Reservation> reservationsForLesson3 = List.of();

        when(reservationRepository.findAllByBookable(lesson))
                .thenReturn(reservationsForLesson1);

        when(reservationRepository.findAllByBookable(lesson2))
                .thenReturn(reservationsForLesson2);

        when(reservationRepository.findAllByBookable(lesson3))
                .thenReturn(reservationsForLesson3);

        tomorrow.set(Calendar.HOUR_OF_DAY, 9);
        String startTime = dateFormat.format(tomorrow.getTime());
        tomorrowButTwoHoursLater.set(Calendar.HOUR_OF_DAY, 11);
        String endTime = dateFormat.format(tomorrowButTwoHoursLater.getTime());

        Timestamp tomorrowTimestamp = parseStringToTimestamp(startTime);
        Timestamp tomorrowPlusTwoHoursTimestamp = parseStringToTimestamp(endTime);

        List<Lesson> allLessons =
                List.of(lesson, lesson2, lesson3);

        when(lessonRepository.findAllByStartTimeAfterAndEndTimeBefore(
                tomorrowTimestamp,
                tomorrowPlusTwoHoursTimestamp
        )).thenReturn(allLessons);

        List<Lesson> returnedLesson =
                lessonService.getAllAvailableLessons(startTime, endTime);

        assertEquals(
                "There should have been 2 lessons available.",
                2,
                returnedLesson.size()
        );

        assertTrue(
                "List should not contain the first lesson",
                !returnedLesson.contains(lesson)
        );

        assertTrue(
                "List should contain the second lesson",
                returnedLesson.contains(lesson2)
        );

        assertTrue(
                "List should contain the third lesson",
                returnedLesson.contains(lesson3)
        );
    }

    @Test
    public void removeLessonBooking_DeletesCorrectBooking_WhenParametersValid() {
        when(reservationRepository.findById(reservationUuid))
                .thenReturn(Optional.of(reservationToRemove));

        lessonService.removeLessonBooking(reservationUuid, reservationTeamUuid);
        verify(reservationRepository, times(1)).delete(reservationToRemove);
        verify(usersClient, times(1))
                .deleteReservationForTeam(reservationUuid, reservationTeamUuid);
    }

    @Test
    public void removeLessonBooking_ThrowsException_WhenReservationDoesNotExist() {
        when(reservationRepository.findById(reservationUuid)).thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () ->   lessonService.removeLessonBooking(
                        reservationUuid,
                        reservationTeamUuid
                )
        );

        verify(usersClient, never())
                .deleteReservationForTeam(reservationUuid, reservationTeamUuid);
    }

    @Test
    public void removeLessonBooking_ThrowsException_WhenADifferentTeamTriesToDelete() {
        when(reservationRepository.findById(reservationUuid))
                .thenReturn(Optional.of(reservationToRemove));

        assertThrows(
                InvalidParameterException.class,
                () -> lessonService.removeLessonBooking(
                        reservationUuid,
                        otherTeamUuid
                )
        );

        verify(usersClient, never())
                .deleteReservationForTeam(reservationUuid, reservationTeamUuid);
    }

    private Timestamp parseStringToTimestamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                Locale.GERMANY
        );
        Date date = null;

        try {
            date = format.parse(time);
        } catch (Exception e) {
            throw new InvalidParameterException("Timestamp cannot be parsed.");
        }

        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp;
    }
}
