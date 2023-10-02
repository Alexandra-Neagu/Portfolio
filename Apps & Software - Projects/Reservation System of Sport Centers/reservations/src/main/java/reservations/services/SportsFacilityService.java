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
import reservations.entities.Bookable;
import reservations.entities.Equipment;
import reservations.entities.Reservation;
import reservations.entities.SportsFacility;
import reservations.repositories.ReservationRepository;
import reservations.repositories.SportsFacilityRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;
import reservations.validators.equipment.EquipmentExistsValidator;
import reservations.validators.facility.SportsFacilityExistsValidator;
import reservations.validators.facility.SportsFacilityIsAvailableValidator;
import reservations.validators.team.TeamCanBookValidator;
import reservations.validators.team.TeamExistsValidator;
import reservations.validators.time.FutureTimestampValidator;
import reservations.validators.time.StringToTimestampValidator;
import reservations.validators.time.TimestampWithinBoundsValidator;

@Service
public class SportsFacilityService {
    private SportsFacilityRepository sportsFacilityRepository;
    private ReservationRepository reservationRepository;
    private UsersClient usersClient;

    private LocalTime reservationStart = LocalTime.of(16, 0, 0);
    private LocalTime reservationEnd = LocalTime.of(23, 0, 0);

    /**
     * Constructor for the facility service.
     */
    @Autowired
    public SportsFacilityService(
            SportsFacilityRepository sportsFacilityRepository,
            ReservationRepository reservationRepository,
            UsersClient usersClient
    ) {
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.reservationRepository = reservationRepository;
        this.usersClient = usersClient;
    }

    /**
     * Add a new facility to the database.
     *
     * @param name - the name of the facility
     * @param minCapacity - the minimum capacity of the facility
     * @param maxCapacity - the maximum capacity of the facility
     */
    public void addSportsFacility(String name, int minCapacity, int maxCapacity) {
        SportsFacility sportsFacility = new SportsFacility(name, minCapacity, maxCapacity);
        sportsFacilityRepository.save(sportsFacility);
    }

    /**
     * Deletes a sports facility given its UUID. Will also delete all reservations,
     * related to that facility.
     *
     * @param sportsFacilityUuid The UUID of the facility to delete
     */
    public void deleteSportsFacility(UUID sportsFacilityUuid) {
        SportsFacilityExistsValidator validator =
                new SportsFacilityExistsValidator(sportsFacilityRepository);
        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(sportsFacilityUuid, null, null, 1);
        validator.handle(uncheckedReservation);

        SportsFacility sportsFacility =
                (SportsFacility) uncheckedReservation.getConvertedBookable();

        List<Reservation> reservationsOfSportsFacility =
                reservationRepository.findAllByBookable(sportsFacility);

        reservationRepository.deleteAll(reservationsOfSportsFacility);

        for (Reservation reservation : reservationsOfSportsFacility) {
            usersClient.deleteReservationForTeam(
                    reservation.getReservationUUID(),
                    reservation.getTeamUUID()
            );
        }

        sportsFacilityRepository.delete(sportsFacility);
    }

    /**
     * Updates the properties of the sports facility with the provided sportsFacilityUuid.
     *
     * @param sportsFacilityUuid The UUID of the facility to update
     * @param newName The new name of the facility
     * @param newMinCapacity The new minimum capacity of the facility
     * @param newMaxCapacity The new maximum capacity of the facility
     */
    public void updateSportsFacility(
            UUID sportsFacilityUuid,
            String newName,
            int newMinCapacity,
            int newMaxCapacity
    ) {
        SportsFacilityExistsValidator validator =
                new SportsFacilityExistsValidator(sportsFacilityRepository);

        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(sportsFacilityUuid, null, null, 1);

        validator.handle(uncheckedReservation);

        SportsFacility sportsFacility =
                (SportsFacility) uncheckedReservation.getConvertedBookable();

        sportsFacility.setName(newName);
        sportsFacility.setMinCapacity(newMinCapacity);
        sportsFacility.setMaxCapacity(newMaxCapacity);
        sportsFacilityRepository.save(sportsFacility);
    }

    /**
     * Creates a reservation for a sports facility for the given team with the
     * provided start time. Checks whether the team and the sports facilities
     * are valid before doing so.
     *
     * @param teamUuid - The UUID of the team to make the reservation for
     * @param sportsFacilityUuid - The UUID of the sports facility to book
     * @param startTime - the start time of the reservation. End time is automatically
     *                  inferred to be one hour after the start time.
     */
    public void bookSportsFacility(UUID teamUuid, UUID sportsFacilityUuid, String startTime) {
        Validator sportsFacilityExistsValidator =
                new SportsFacilityExistsValidator(sportsFacilityRepository);

        Validator teamExistsValidator = new TeamExistsValidator(usersClient);
        Validator teamCanBookValidator = new TeamCanBookValidator(usersClient);

        sportsFacilityExistsValidator.setNext(teamExistsValidator);
        teamExistsValidator.setNext(teamCanBookValidator);

        Validator stringToTimestampValidator = new StringToTimestampValidator();
        Validator timestampWithinBoundsValidator = new TimestampWithinBoundsValidator(
                reservationStart, reservationEnd, "book a sports facility"
        );

        Validator timestampInTheFuture = new FutureTimestampValidator();

        teamCanBookValidator.setNext(stringToTimestampValidator);
        stringToTimestampValidator.setNext(timestampWithinBoundsValidator);
        timestampWithinBoundsValidator.setNext(timestampInTheFuture);

        Validator sportsFacilityIsAvailableValidator =
                new SportsFacilityIsAvailableValidator(reservationRepository, usersClient);

        timestampInTheFuture.setNext(sportsFacilityIsAvailableValidator);

        UncheckedReservation uncheckedReservation = new UncheckedReservation(
                sportsFacilityUuid, teamUuid, startTime, 1
        );

        sportsFacilityExistsValidator.handle(uncheckedReservation);

        Reservation reservation = new Reservation(
                teamUuid,
                uncheckedReservation.getConvertedBookable(),
                uncheckedReservation.getConvertedTimestamp()
        );

        reservationRepository.save(reservation);

        usersClient.addReservationForTeam(
                teamUuid,
                reservation.getReservationUUID(),
                startTime
        );
    }

    /**
     * Removes a booking for a sports facility, given the reservation UUID and the
     * team UUID.
     *
     * @param reservationUuid - The reservation that has to be cancelled
     * @param teamUuid - The team that is trying to cancel the reservation. If another team
     *                 tries to cancel the request of the one that made the reservation, the
     *                 method will throw an InvalidParameterException
     */
    public void removeSportsFacilityBooking(UUID reservationUuid, UUID teamUuid) {
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
     * Retrieves a sports facility from the repository given its UUID.
     *
     * @param uuid - The uuid of the facility to retrieve
     * @return The SportsFacility corresponding to this UUID
     * @throws InvalidParameterException If there is no SportsFacility with this UUID
     */
    public SportsFacility getSportsFacility(UUID uuid) {
        return sportsFacilityRepository.findById(uuid).orElseThrow(
                () -> new InvalidParameterException("The sports facility doesn't exist.")
        );
    }

    /**
     * Retrieves all sports facilities from the repository.
     *
     * @return A list containing information about all sports facilities.
     */
    public List<SportsFacility> getAllSportsFacilities() {
        return sportsFacilityRepository.findAll();
    }

    /**
     * Gets the sports facilities that are available at a certain time.
     *
     * @param startTime - the time to check which facilities are available for
     * @return - A list of all available sports facilities, which have not yet been
     *     booked.
     */
    public List<SportsFacility> getAllAvailableSportsFacilities(String startTime) {
        Validator stringToTimestampValidator = new StringToTimestampValidator();
        Validator timestampWithinBoundsValidator = new TimestampWithinBoundsValidator(
                reservationStart, reservationEnd, "book sports facility"
        );
        Validator timestampInTheFuture = new FutureTimestampValidator();

        timestampWithinBoundsValidator.setNext(timestampInTheFuture);
        stringToTimestampValidator.setNext(timestampWithinBoundsValidator);

        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(null, null, startTime, 0);

        stringToTimestampValidator.handle(uncheckedReservation);

        Timestamp startTimeTimestamp = uncheckedReservation.getConvertedTimestamp();
        long millisecondsInHour = 3_600_000L;

        Timestamp overlappingReservationStart =
                new Timestamp(startTimeTimestamp.getTime() - millisecondsInHour + 1);
        Timestamp overlappingReservationEnd =
                new Timestamp(startTimeTimestamp.getTime() + millisecondsInHour - 1);

        List<SportsFacility> allSportsFacilities = getAllSportsFacilities();

        List<Bookable> bookedBookables = reservationRepository
                .findAllByStartTimeBetween(overlappingReservationStart, overlappingReservationEnd)
                .stream().map(Reservation::getBookable).collect(Collectors.toList());

        List<SportsFacility> availableSportsFacilities =
                allSportsFacilities.stream().filter(x -> !bookedBookables.contains(x))
                        .collect(Collectors.toList());

        return availableSportsFacilities;
    }
}
