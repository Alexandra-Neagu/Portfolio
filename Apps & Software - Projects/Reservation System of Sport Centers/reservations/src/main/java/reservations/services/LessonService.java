package reservations.services;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reservations.clients.UsersClient;
import reservations.entities.Lesson;
import reservations.entities.Reservation;
import reservations.entities.SportsFacility;
import reservations.repositories.LessonRepository;
import reservations.repositories.ReservationRepository;
import reservations.repositories.SportsFacilityRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;
import reservations.validators.facility.SportsFacilityExistsValidator;
import reservations.validators.facility.SportsFacilityIsAvailableValidator;
import reservations.validators.lesson.LessonExistsValidator;
import reservations.validators.lesson.LessonIsAvailableValidator;
import reservations.validators.team.TeamCanBookValidator;
import reservations.validators.team.TeamExistsValidator;
import reservations.validators.time.FutureTimestampValidator;
import reservations.validators.time.StringToTimestampValidator;
import reservations.validators.time.TimestampToStringValidator;
import reservations.validators.time.TimestampWithinBoundsValidator;

@Service
public class LessonService {
    private LessonRepository lessonRepository;
    private SportsFacilityRepository sportsFacilityRepository;
    private ReservationRepository reservationRepository;
    private UsersClient usersClient;

    private LocalTime reservationStart = LocalTime.of(9, 0, 0);
    private LocalTime reservationEnd = LocalTime.of(16, 0, 0);

    /**
     * A constructor for the LessonService.
     */
    @Autowired
    public LessonService(
            LessonRepository lessonRepository,
            SportsFacilityRepository sportsFacilityRepository,
            ReservationRepository reservationRepository,
            UsersClient usersClient
    ) {
        this.lessonRepository = lessonRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.reservationRepository = reservationRepository;
        this.usersClient = usersClient;
    }

    /**
     * Add a new lesson to the database.
     *
     * @param name - the name of the lesson
     * @param maxCapacity - the maximum capacity of the lesson
     * @param sportsFacilityUuid - the uuid of the sports facility in which the lesson takes place
     * @param startTime - the starting time of the lesson
     * @param endTime - the ending time of the lesson
     */
    public void addLesson(String name,
                          int maxCapacity,
                          UUID sportsFacilityUuid,
                          String startTime,
                          String endTime) {

        Validator sportsFacilityExistsValidator =
                new SportsFacilityExistsValidator(sportsFacilityRepository);

        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(sportsFacilityUuid, null, null, 1);

        sportsFacilityExistsValidator.handle(uncheckedReservation);

        Timestamp startTimestamp = parseStringToTimestamp(startTime);
        Timestamp endTimestamp = parseStringToTimestamp(endTime);

        if (endTimestamp.before(startTimestamp)) {
            throw new InvalidParameterException(
                    "End time cannot be before the start time"
            );
        }

        SportsFacility sportsFacility =
                (SportsFacility) uncheckedReservation.getConvertedBookable();

        List<Lesson> lessonsInOverlappingTimeslot =
                lessonRepository.findDistinctBySportsFacilityAndStartTimeBetween(
                        sportsFacility,
                        startTimestamp,
                        endTimestamp
                );

        if (lessonsInOverlappingTimeslot.size() > 0) {
            throw new InvalidParameterException(
                    "This hall already has a lesson during that time period"
            );
        }

        Lesson lesson = new Lesson(name, maxCapacity, sportsFacility, startTimestamp, endTimestamp);
        lessonRepository.save(lesson);
    }

    /**
     * Deletes a lesson item given its UUID. Will also delete all reservations,
     * related to that lesson.
     *
     * @param lessonUuid The UUID of the lesson to delete
     */
    public void deleteLesson(UUID lessonUuid) {
        LessonExistsValidator validator = new LessonExistsValidator(lessonRepository);
        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(lessonUuid, null, null, 1);
        validator.handle(uncheckedReservation);

        Lesson lesson = (Lesson) uncheckedReservation.getConvertedBookable();

        List<Reservation> reservationsOfLesson = reservationRepository.findAllByBookable(lesson);
        reservationRepository.deleteAll(reservationsOfLesson);

        for (Reservation reservation : reservationsOfLesson) {
            usersClient.deleteReservationForTeam(
                    reservation.getReservationUUID(),
                    reservation.getTeamUUID()
            );
        }

        lessonRepository.delete(lesson);
    }

    /**
     * Updates the properties of the lesson with the provided lessonUuid.
     *
     * @param lessonUuid The UUID of the lesson to update
     * @param newName The new name of the lesson
     * @param newMaxCapacity The new maximum capacity of the lesson
     * @param newSportsFacilityUuid The UUID of the new sports facility
     *                              in which the lesson takes place
     * @param newStartTime The new starting time of the lesson
     * @param newEndTime The new ending time of the lesson
     */
    public void updateLesson(
            UUID lessonUuid,
            String newName,
            int newMaxCapacity,
            UUID newSportsFacilityUuid,
            String newStartTime,
            String newEndTime
    ) {
        LessonExistsValidator lessonValidator = new LessonExistsValidator(lessonRepository);
        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(lessonUuid, null, null, 1);

        lessonValidator.handle(uncheckedReservation);

        final Lesson lesson = (Lesson) uncheckedReservation.getConvertedBookable();

        SportsFacilityExistsValidator facilityExistsValidator =
                new SportsFacilityExistsValidator(sportsFacilityRepository);

        uncheckedReservation =
                new UncheckedReservation(newSportsFacilityUuid, null, null, 1);

        facilityExistsValidator.handle(uncheckedReservation);
        final SportsFacility facility =
                (SportsFacility) uncheckedReservation.getConvertedBookable();

        Timestamp newStartTimestamp = parseStringToTimestamp(newStartTime);
        Timestamp newEndTimestamp = parseStringToTimestamp(newEndTime);

        if (newEndTimestamp.before(newStartTimestamp)) {
            throw new InvalidParameterException(
                    "End time cannot be before the start time"
            );
        }

        List<Lesson> lessonsInOverlappingTimeslot =
                lessonRepository.findDistinctBySportsFacilityAndStartTimeBetween(
                        facility,
                        newStartTimestamp,
                        newEndTimestamp
                );

        if (lessonsInOverlappingTimeslot.size() > 0) {
            throw new InvalidParameterException(
                    "This hall already has a lesson during that time period"
            );
        }

        lesson.setName(newName);
        lesson.setMaxCapacity(newMaxCapacity);
        lesson.setSportsFacility(facility);
        lesson.setStartTime(newStartTimestamp);
        lesson.setEndTime(newEndTimestamp);
        lessonRepository.save(lesson);
    }

    /**
     * Parses a string to a timestamp.
     */
    public Timestamp parseStringToTimestamp(String time) {
        StringToTimestampValidator stringToTimestampValidator = new StringToTimestampValidator();
        FutureTimestampValidator futureTimestampValidator = new FutureTimestampValidator();
        TimestampWithinBoundsValidator timestampWithinBoundsValidator =
                new TimestampWithinBoundsValidator(reservationStart, reservationEnd, "add lesson");

        stringToTimestampValidator.setNext(futureTimestampValidator);
        futureTimestampValidator.setNext(timestampWithinBoundsValidator);

        UncheckedReservation reservation = new UncheckedReservation(null, null, time, 1);

        reservation.setTimestampString(time);
        stringToTimestampValidator.handle(reservation);

        return reservation.getConvertedTimestamp();
    }

    /**
     * Creates a reservation for a lesson for the given team with the
     * provided start time. Checks whether the team and the lessons
     * are valid before doing so.
     *
     * @param teamUuid - The UUID of the team to make the reservation for
     * @param lessonUuid - The UUID of the lesson to book
     */
    public void bookLesson(UUID teamUuid, UUID lessonUuid) {
        Validator lessonExistsValidator = new LessonExistsValidator(lessonRepository);
        Validator teamExistsValidator = new TeamExistsValidator(usersClient);

        lessonExistsValidator.setNext(teamExistsValidator);

        Validator timestampToStringValidator = new TimestampToStringValidator();
        Validator canTeamBookValidator = new TeamCanBookValidator(usersClient);
        Validator lessonIsAvailableValidator =
                new LessonIsAvailableValidator(reservationRepository, usersClient);

        teamExistsValidator.setNext(timestampToStringValidator);
        timestampToStringValidator.setNext(canTeamBookValidator);
        canTeamBookValidator.setNext(lessonIsAvailableValidator);

        UncheckedReservation uncheckedReservation = new UncheckedReservation(
                lessonUuid, teamUuid, null, 1
        );

        lessonExistsValidator.handle(uncheckedReservation);

        Lesson lesson = (Lesson) uncheckedReservation.getConvertedBookable();
        int teamMembers = uncheckedReservation.getAmount();

        Reservation reservation =
                new Reservation(teamUuid, lesson, lesson.getStartTime(), teamMembers);

        reservationRepository.save(reservation);

        String timestampString = uncheckedReservation.getTimestampString();

        usersClient.addReservationForTeam(
                teamUuid,
                reservation.getReservationUUID(),
                timestampString
        );
    }

    /**
     * Removes a booking for a lesson, given the reservation UUID and the
     * team UUID.
     *
     * @param reservationUuid - The reservation that has to be cancelled
     * @param teamUuid - The team that is trying to cancel the reservation. If another team
     *                 tries to cancel the request of the one that made the reservation, the
     *                 method will throw an InvalidParameterException
     */
    public void removeLessonBooking(UUID reservationUuid, UUID teamUuid) {
        Reservation reservation = reservationRepository.findById(reservationUuid).orElseThrow(
                () -> new InvalidParameterException("Booking with this UUID does not exist")
        );

        if (!reservation.getTeamUUID().equals(teamUuid)) {
            throw new InvalidParameterException("You cannot cancel another team's booking");
        }

        usersClient.deleteReservationForTeam(reservationUuid, teamUuid);
        reservationRepository.delete(reservation);
    }

    /**
     * Retrieves a lesson from the repository given its UUID.
     *
     * @param uuid - The uuid of the lesson to retrieve
     * @return The Lesson corresponding to this UUID
     * @throws InvalidParameterException If there is no Lesson with this UUID
     */
    public Lesson getLesson(UUID uuid) {
        return lessonRepository.findById(uuid).orElseThrow(
                () -> new InvalidParameterException("The lesson doesn't exist.")
        );
    }

    /**
     * Retrieves all lessons from the repository.
     *
     * @return A list containing information about all lessons.
     */
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    /**
     * Gets the lessons that are available at a certain time.
     *
     * @param startTime - the time to check which lessons are available for
     * @return - A list of all available sports lessons, which have not yet been
     *           booked.
     */
    public List<Lesson> getAllAvailableLessons(String startTime, String endTime) {
        Timestamp startTimestamp = parseStringToTimestamp(startTime);
        Timestamp endTimestamp = parseStringToTimestamp(endTime);

        List<Lesson> allLessons =
                lessonRepository.findAllByStartTimeAfterAndEndTimeBefore(
                        startTimestamp,
                        endTimestamp
                );

        for (Lesson lesson : allLessons) {
            List<Reservation> reservationsForLesson =
                    reservationRepository.findAllByBookable(lesson);

            int takenSpots = reservationsForLesson.stream().mapToInt(Reservation::getAmount).sum();
            lesson.setMaxCapacity(lesson.getMaxCapacity() - takenSpots);
        }

        return allLessons.stream()
                .filter(x -> x.getMaxCapacity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a sports facility from the repository.
     *
     * @param uuid The Uuid of the sports facility
     * @return A SportsFacility object, corresponding to the provided Uuid
     */
    public SportsFacility getSportsFacility(UUID uuid) {
        return sportsFacilityRepository.findById(uuid).orElseThrow(() ->
                new InvalidParameterException("A sports facility with this UUID does not exist.")
        );
    }
}
