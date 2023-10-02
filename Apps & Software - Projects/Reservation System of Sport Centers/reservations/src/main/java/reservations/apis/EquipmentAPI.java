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
import reservations.entities.Equipment;
import reservations.services.EquipmentService;

@RestController
@RequestMapping("api/v1/equipment")
public class EquipmentAPI {
    private final EquipmentService equipmentService;

    public EquipmentAPI(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    private final String equipmentUuidLiteral = "equipmentUuid";

    /**
     * PUT Endpoint to add a piece of equipment to the database.
     *
     * @param name the name given to the equipment
     * @param capacity the amount of units
     * @param relatedSport the sport related to the equipment
     */
    @PutMapping("add")
    public void addEquipment(@RequestParam("name") String name,
                             @RequestParam("capacity") int capacity,
                             @RequestParam("relatedSport") String relatedSport) {
        equipmentService.addEquipment(name, capacity, relatedSport);
    }

    /**
     * A Delete endpoint to remove a piece of equipment.
     *
     * @param equipmentUuid The UUID of the equipment to remove
     */
    @DeleteMapping("delete")
    public void deleteEquipment(@RequestParam(equipmentUuidLiteral) UUID equipmentUuid) {
        equipmentService.deleteEquipment(equipmentUuid);
    }

    /**
     * An update endpoint to set the fields of the equipment.
     *
     * @param equipmentUuid The UUID of the equipment
     * @param newName The new name to use
     * @param newCapacity The new capacity to use
     * @param newRelatedSport The new related sport to use
     */
    @PutMapping("update")
    public void updateEquipment(
            @RequestParam(equipmentUuidLiteral) UUID equipmentUuid,
            @RequestParam("name") String newName,
            @RequestParam("capacity") int newCapacity,
            @RequestParam("relatedSport") String newRelatedSport
    ) {
        equipmentService.updateEquipment(equipmentUuid, newName, newCapacity, newRelatedSport);
    }

    /**
     * PUT Endpoint to book a piece of equipment from the database.
     *
     * @param teamUuid the UUID of the team making the booking
     * @param equipmentUuid the UUID of the equipment requested for booking
     * @param startTime the starting time of the booking
     * @param amount the amount of pieces of equipment requested for booking
     */
    @PutMapping("book")
    public void bookEquipment(@RequestParam("teamUuid") UUID teamUuid,
                              @RequestParam(equipmentUuidLiteral) UUID equipmentUuid,
                              @RequestParam("startTime") String startTime,
                              @RequestParam("amount") int amount) {
        equipmentService.bookEquipment(teamUuid, equipmentUuid, startTime, amount);
    }

    /**
     * PUT Endpoint to delete booking from the database.
     *
     * @param reservationUuid the UUID of the reservation to be deleted
     * @param teamUuid the UUID of the team that made the reservation
     */
    @PutMapping("deleteBooking")
    public void removeEquipmentBooking(@RequestParam("reservationUuid") UUID reservationUuid,
                                       @RequestParam("teamUuid") UUID teamUuid) {
        equipmentService.removeEquipmentBooking(reservationUuid, teamUuid);
    }

    /**
     * GET Endpoint to retrieve a piece of equipment.
     *
     * @param uuid the UUID of the equipment to be retrieved
     * @return response object with 200 OK status code
     */
    @GetMapping("get")
    public ResponseEntity<Equipment> getEquipment(@RequestParam(equipmentUuidLiteral) UUID uuid) {
        Equipment equipment = equipmentService.getEquipment(uuid);
        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    /**
     * GET Endpoint to retrieve all pieces of equipment.
     *
     * @return response object with 200 OK status code
     */
    @GetMapping("getAll")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> allEquipment = equipmentService.getAllEquipment();
        return new ResponseEntity<>(allEquipment, HttpStatus.OK);
    }

    /**
     * GET Endpoint to retrieve all available pieces of equipment for a given time.
     *
     * @param startTime time for which to check which equipment is available
     * @return response object with 200 OK status code
     */
    @GetMapping("getAllAvailable")
    public ResponseEntity<List<Equipment>> getAllAvailableEquipment(
            @RequestParam("startTime") String startTime
    ) {
        List<Equipment> allAvailableEquipment =
                equipmentService.getAllAvailableEquipment(startTime);

        return new ResponseEntity<>(allAvailableEquipment, HttpStatus.OK);
    }
    
    /**
     * An exception handler for when the input is invalid.
     *
     * @param exception exception that has occurred
     * @return response body containing the reason for the exception with a 400 status code
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
