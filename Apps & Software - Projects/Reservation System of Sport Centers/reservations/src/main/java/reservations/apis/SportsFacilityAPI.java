package reservations.apis;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reservations.entities.SportsFacility;
import reservations.services.SportsFacilityService;

@RestController
@RequestMapping("api/v1/facility")
public class SportsFacilityAPI {
    private final SportsFacilityService sportsFacilityService;

    public SportsFacilityAPI(SportsFacilityService sportsFacilityService) {
        this.sportsFacilityService = sportsFacilityService;
    }

    private final String facilityUuidLiteral = "sportsFacilityUuid";

    /**
     * PUT endpont to add a sports facility to the database.
     *
     * @param name The name of the sports facility
     * @param minCapacity The minimum amount of people this sports facility can be booked in
     *                    teams with
     * @param maxCapacity The maximum amount of people this sports facility can be booked in
     *                    teams with
     */
    @PutMapping("add")
    public void addSportsFacility(@RequestParam("name") String name,
                                             @RequestParam("minCapacity") int minCapacity,
                                             @RequestParam("maxCapacity") int maxCapacity
    ) {
        sportsFacilityService.addSportsFacility(name, minCapacity, maxCapacity);
    }

    /**
     * A Delete endpoint to remove the sports facility with the provided UUID.
     *
     * @param sportsFacilityUuid The UUID of the sports facility to remove
     */
    @DeleteMapping("delete")
    public void deleteSportsFacility(@RequestParam(facilityUuidLiteral) UUID sportsFacilityUuid) {
        sportsFacilityService.deleteSportsFacility(sportsFacilityUuid);
    }

    /**
     * An Update endpoint to update a facility with the provided UUID with new properties.
     *
     * @param sportsFacilityUuid The UUID of the facility to update
     * @param newName The new name of the facility
     * @param newMinCapacity The new minimum capacity of the facility
     * @param newMaxCapacity The new maximum capacity of the facility
     */
    @PutMapping("update")
    public void updateSportsFacility(
            @RequestParam(facilityUuidLiteral) UUID sportsFacilityUuid,
            @RequestParam("name") String newName,
            @RequestParam("minCapacity") int newMinCapacity,
            @RequestParam("maxCapacity") int newMaxCapacity) {
        sportsFacilityService.updateSportsFacility(
                sportsFacilityUuid,
                newName,
                newMinCapacity,
                newMaxCapacity
        );
    }

    /**
     * PUT endpoint for a team to book a sports facility.
     *
     * @param teamUuid The UUID of the team that is trying to make the booking
     * @param sportsFacilityUuid The UUID of the facility that this team wishes to book
     * @param startTime The start time of the reservation. End time is automatically set
     *                  to 1 hour after the start time
     */
    @PutMapping("book")
    public void bookSportsFacility(
            @RequestParam("teamUuid") UUID teamUuid,
            @RequestParam(facilityUuidLiteral) UUID sportsFacilityUuid,
            @RequestParam("startTime") String startTime
    ) {
        sportsFacilityService.bookSportsFacility(teamUuid, sportsFacilityUuid, startTime);
    }

    /**
     * PUT endpoint to delete a reservation for a sports facility.
     *
     * @param reservationUuid The UUID of the reservation to remove
     * @param teamUuid The team that is trying to remove the booking. Only teams that have made
     *                 the reservation with this reservationUUID have a right to cancel that
     *                 booking
     */
    @PutMapping("deleteBooking")
    public void removeSportsFacilityBooking(
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("teamUuid") UUID teamUuid
    ) {
        sportsFacilityService.removeSportsFacilityBooking(reservationUuid, teamUuid);
    }

    /**
     * GET endpoint to retrieve a sports facility by its UUID.
     *
     * @param uuid The UUID of the sports facility
     * @return A SportsFacility, corresponding to this UUID
     */
    @GetMapping("get")
    public ResponseEntity<SportsFacility> getSportsFacility(
            @RequestParam(facilityUuidLiteral) UUID uuid
    ) {
        SportsFacility sportsFacility = sportsFacilityService.getSportsFacility(uuid);
        return new ResponseEntity<>(sportsFacility, HttpStatus.OK);
    }

    /**
     * GET endpoint to retrieve all sports facilities residing in the database.
     *
     * @return a list of all sportsfacilities in the database
     */
    @GetMapping("getAll")
    public ResponseEntity<List<SportsFacility>> getAllSportsFacilities() {
        List<SportsFacility> allSportsFacilities = sportsFacilityService.getAllSportsFacilities();
        return new ResponseEntity<>(allSportsFacilities, HttpStatus.OK);
    }

    /**
     * A GET endpoint to get all available sports facilities given a time.
     *
     * @param startTime - The time to check the availability of the sports facilities
     *                  for
     * @return A list of sports facilities which are available during the indicated
     *     time.
     */
    @GetMapping("getAllAvailable")
    public ResponseEntity<List<SportsFacility>> getAllAvailableSportsFacilities(
            @RequestParam("startTime") String startTime
    ) {
        List<SportsFacility> allAvailableSportsFacilities =
                sportsFacilityService.getAllAvailableSportsFacilities(startTime);

        return new ResponseEntity<>(allAvailableSportsFacilities, HttpStatus.OK);
    }

    /**
     * An exception handler for when one or more of the request parameters are invalid.
     *
     * @param exception The exception that was originally thrown
     * @return A `BAD_REQUEST` response with a message, corresponding to the one in the
     *         exception that was thrown
     */
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Object> badParameterExceptionHandler(
            InvalidParameterException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
