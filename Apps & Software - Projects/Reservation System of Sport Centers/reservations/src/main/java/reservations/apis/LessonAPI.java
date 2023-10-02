package reservations.apis;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
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
import reservations.entities.Lesson;
import reservations.services.LessonService;

@RestController
@RequestMapping("api/v1/lesson")
public class LessonAPI {
    private final LessonService lessonService;

    public LessonAPI(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    private final String lessonUuidLiteral = "lessonUuid";

    /**
     * PUT Endpoint to add a lesson to the database.
     *
     * @param name the name given to the lesson
     * @param maxCapacity the amount of units
     * @param sportsFacilityUuid the Uuid of the facility in which the lesson is to take place
     * @param startTime the starting time of the lesson
     * @param endTime the ending time of the lesson
     */
    @PutMapping("add")
    public void addLesson(@RequestParam("name") String name,
                          @RequestParam("maxCapacity") int maxCapacity,
                          @RequestParam("sportsFacilityUuid") UUID sportsFacilityUuid,
                          @RequestParam("startTime") String startTime,
                          @RequestParam("endTime") String endTime) {
        lessonService.addLesson(name, maxCapacity, sportsFacilityUuid, startTime, endTime);
    }

    /**
     * A Delete endpoint to delete a lesson with a certain Uuid.
     *
     * @param lessonUuid The Uuid of the lesson to delete
     */
    @DeleteMapping("delete")
    public void deleteLesson(@RequestParam(lessonUuidLiteral) UUID lessonUuid) {
        lessonService.deleteLesson(lessonUuid);
    }

    /**
     * An Update endpoint to update a lesson with new properties.
     *
     * @param lessonUuid The Uuid of the lesson to update
     * @param newName The new name of the lesson
     * @param newMaxCapacity The new max capacity of the lesson
     * @param newSportsFacilityUuid The new UUID of the sports facility that is being used
     * @param newStartTime The new start time of the lesson
     * @param newEndTime The new end time of the lesson
     */
    @PutMapping("update")
    public void updateLesson(
            @RequestParam(lessonUuidLiteral) UUID lessonUuid,
            @RequestParam("name") String newName,
            @RequestParam("maxCapacity") int newMaxCapacity,
            @RequestParam("sportsFacilityUuid") UUID newSportsFacilityUuid,
            @RequestParam("startTime") String newStartTime,
            @RequestParam("endTime") String newEndTime
    ) {
        lessonService.updateLesson(
                lessonUuid,
                newName,
                newMaxCapacity,
                newSportsFacilityUuid,
                newStartTime,
                newEndTime
        );
    }

    /**
     * PUT Endpoint to book a piece of lesson from the database.
     *
     * @param teamUuid the UUID of the team making the booking
     * @param lessonUuid the UUID of the lesson requested for booking
     */
    @PutMapping("book")
    public void bookLesson(
            @RequestParam("teamUuid") UUID teamUuid,
            @RequestParam(lessonUuidLiteral) UUID lessonUuid
    ) {
        lessonService.bookLesson(teamUuid, lessonUuid);
    }

    /**
     * PUT Endpoint to delete booking from the database.
     *
     * @param reservationUuid the UUID of the reservation to be deleted
     * @param teamUuid the UUID of the team that made the reservation
     */
    @PutMapping("deleteBooking")
    public void removeLessonBooking(
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("teamUuid") UUID teamUuid
    ) {
        lessonService.removeLessonBooking(reservationUuid, teamUuid);
    }

    /**
     * GET Endpoint to retrieve a lesson.
     *
     * @param uuid the UUID of the lesson to be retrieved
     * @return response object with 200 OK status code
     */
    @GetMapping("get")
    public ResponseEntity<Lesson> getLesson(@RequestParam(lessonUuidLiteral) UUID uuid) {
        Lesson lesson = lessonService.getLesson(uuid);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    /**
     * GET Endpoint to retrieve all lessons.
     *
     * @return response object with 200 OK status code
     */
    @GetMapping("getAll")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> allLessons = lessonService.getAllLessons();
        return new ResponseEntity<>(allLessons, HttpStatus.OK);
    }

    /**
     * GET Endpoint to retrieve all available lessons for a given time.
     *
     * @param startTime time for which to check which lesson is available
     * @return response object with 200 OK status code
     */
    @GetMapping("getAllAvailable")
    public ResponseEntity<List<Lesson>> getAllAvailableLessons(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime
    ) {
        List<Lesson> allAvailableLessons =
                lessonService.getAllAvailableLessons(startTime, endTime);

        return new ResponseEntity<>(allAvailableLessons, HttpStatus.OK);
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
