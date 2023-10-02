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
import reservations.entities.Equipment;
import reservations.entities.Reservation;
import reservations.repositories.EquipmentRepository;
import reservations.repositories.ReservationRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;
import reservations.validators.equipment.EquipmentExistsValidator;
import reservations.validators.equipment.EquipmentIsAvailableValidator;
import reservations.validators.team.TeamExistsValidator;
import reservations.validators.time.FutureTimestampValidator;
import reservations.validators.time.StringToTimestampValidator;
import reservations.validators.time.TimestampWithinBoundsValidator;

@Service
public class EquipmentService {
    private EquipmentRepository equipmentRepository;
    private ReservationRepository reservationRepository;
    private UsersClient usersClient;

    private LocalTime reservationStart = LocalTime.of(16, 0, 0);
    private LocalTime reservationEnd = LocalTime.of(23, 0, 0);

    /**
     * A constructor for the equipment service.
     */
    @Autowired
    public EquipmentService(
            EquipmentRepository equipmentRepository,
            ReservationRepository reservationRepository,
            UsersClient usersClient
    ) {
        this.equipmentRepository = equipmentRepository;
        this.usersClient = usersClient;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Adds a new piece of equipment to database.
     *
     * @param name the name of the equipment
     * @param capacity the amount of equipments available
     * @param relatedSport the sport that the equipment is used for
     */
    public void addEquipment(String name, int capacity, String relatedSport) {
        Equipment equipment = new Equipment(name, capacity, relatedSport);
        equipmentRepository.save(equipment);
    }

    /**
     * Deletes an equipment item given its UUID. Will also delete all reservations,
     * related to that item.
     *
     * @param equipmentUuid The UUID of the equipment to delete
     */
    public void deleteEquipment(UUID equipmentUuid) {
        EquipmentExistsValidator validator = new EquipmentExistsValidator(equipmentRepository);
        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(equipmentUuid, null, null, 1);
        validator.handle(uncheckedReservation);

        Equipment equipment = (Equipment) uncheckedReservation.getConvertedBookable();

        List<Reservation> reservationsOfEquipment = reservationRepository
                .findAllByBookable(equipment);
        reservationRepository.deleteAll(reservationsOfEquipment);

        equipmentRepository.delete(equipment);
    }

    /**
     * Updates the properties of the piece of equipment with the provided equipmentUuid.
     *
     * @param equipmentUuid The UUID of the equipment to update
     * @param newName The new name of the equipment
     * @param newCapacity The new capacity of the equipment
     * @param newRelatedSport The new related sport of the equipment
     */
    public void updateEquipment(
            UUID equipmentUuid,
            String newName,
            int newCapacity,
            String newRelatedSport
    ) {
        EquipmentExistsValidator validator = new EquipmentExistsValidator(equipmentRepository);
        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(equipmentUuid, null, null, 1);

        validator.handle(uncheckedReservation);

        Equipment equipment = (Equipment) uncheckedReservation.getConvertedBookable();
        equipment.setName(newName);
        equipment.setMaxCapacity(newCapacity);
        equipment.setRelatedSport(newRelatedSport);
        equipmentRepository.save(equipment);
    }

    /**
     * Creates a booking for the equipment.
     *
     * @param teamUuid the team that is making the reservation
     * @param equipmentUuid the id of the equipment
     * @param startTime the starting time of the reservation
     * @param amount the amount of pieces of equipment that the user requests to book
     */
    public void bookEquipment(UUID teamUuid, UUID equipmentUuid, String startTime, int amount) {
        Validator doesTeamExistValidator = new TeamExistsValidator(usersClient);
        Validator equipmentExistsValidator = new EquipmentExistsValidator(equipmentRepository);
        doesTeamExistValidator.setNext(equipmentExistsValidator);

        Validator stringToTimestampValidator = new StringToTimestampValidator();
        Validator timestampWithinBoundsValidator =
                new TimestampWithinBoundsValidator(
                    reservationStart, reservationEnd, "book equipment"
                );

        equipmentExistsValidator.setNext(stringToTimestampValidator);
        stringToTimestampValidator.setNext(timestampWithinBoundsValidator);

        Validator timestampInTheFutureValidator = new FutureTimestampValidator();
        Validator equipmentAmountIsAvailableValidator =
                new EquipmentIsAvailableValidator(reservationRepository);

        timestampWithinBoundsValidator.setNext(timestampInTheFutureValidator);
        timestampInTheFutureValidator.setNext(equipmentAmountIsAvailableValidator);

        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(equipmentUuid, teamUuid, startTime, amount);

        doesTeamExistValidator.handle(uncheckedReservation);

        Reservation reservation = new Reservation(
                teamUuid,
                uncheckedReservation.getConvertedBookable(),
                uncheckedReservation.getConvertedTimestamp(),
                amount
        );

        reservationRepository.save(reservation);
    }

    /**
     * Removes a booking for the equipment.
     *
     * @param reservationUuid the reservation id
     * @param teamUuid the team id
     */
    public void removeEquipmentBooking(UUID reservationUuid, UUID teamUuid) {
        Reservation reservation = reservationRepository.findById(reservationUuid).orElseThrow(
                () -> new InvalidParameterException("Booking with this Uuid does not exist")
        );

        if (!reservation.getTeamUUID().equals(teamUuid)) {
            throw new InvalidParameterException("You cannot cancel another team's reservation");
        }

        reservationRepository.delete(reservation);
    }

    /**
     * Get the specific equipment.
     *
     * @param uuid the UUID of the equipment
     * @return the equipment (based on the unique UUID)
     */
    public Equipment getEquipment(UUID uuid) {
        return equipmentRepository.findById(uuid)
                .orElseThrow(() -> new InvalidParameterException("The equipment doesn't exist."));
    }

    /**
     * Get all the equipment in the database.
     *
     * @return all eqipment in the database
     */
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    /**
     * Get all the equipment that is available to book.
     *
     * @param startTime the time for which to check if the equipment is available
     * @return a list of equipments that is available
     */
    public List<Equipment> getAllAvailableEquipment(String startTime) {
        Timestamp parsedTimestamp = parseStringToTimestamp(startTime);
        return getRemainingEquipmentForTime(parsedTimestamp);
    }

    /**
     * Parses a string to a timestamp.
     */
    public Timestamp parseStringToTimestamp(String time) {
        Validator stringToTimestampValidator = new StringToTimestampValidator();
        Validator timestampWithinBoundsValidator = new TimestampWithinBoundsValidator(
                reservationStart, reservationEnd, "book equipment"
        );
        Validator timestampInTheFuture = new FutureTimestampValidator();

        timestampWithinBoundsValidator.setNext(timestampInTheFuture);
        stringToTimestampValidator.setNext(timestampWithinBoundsValidator);

        UncheckedReservation uncheckedReservation =
                new UncheckedReservation(null, null, time, 0);

        stringToTimestampValidator.handle(uncheckedReservation);
        return uncheckedReservation.getConvertedTimestamp();
    }

    /**
     * Gets a list of available equipment at a certain time.
     */
    public List<Equipment> getRemainingEquipmentForTime(Timestamp startTime) {
        long millisecondsInHour = 3_600_000L;

        Timestamp overlappingReservationStart =
                new Timestamp(startTime.getTime() - millisecondsInHour + 1);
        Timestamp overlappingReservationEnd =
                new Timestamp(startTime.getTime() + millisecondsInHour - 1);

        List<Equipment> allEquipment = getAllEquipment();

        List<Reservation> bookedBookables = reservationRepository
                .findAllByStartTimeBetween(overlappingReservationStart, overlappingReservationEnd);

        for (var reservation : bookedBookables) {
            for (var equipment : allEquipment) {
                if (reservation.getBookable().equals(equipment)) {
                    equipment.setMaxCapacity(equipment.getMaxCapacity() - reservation.getAmount());
                    break;
                }
            }
        }

        List<Equipment> availableEquipment =
                allEquipment.stream().filter(x -> x.getMaxCapacity() > 0)
                        .collect(Collectors.toList());

        return availableEquipment;
    }
}
