package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Room controller.
 */
@Controller
@RestController
@RequestMapping("rooms")
public class RoomController {

    private final RoomService roomService;

    /**
     * Instantiates a new Room controller.
     *
     * @param roomService the room service
     */
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Save room.
     *
     * @param room the room
     * @return the room
     */
    @PostMapping("/insert")
    Room save(@RequestBody Room room) {
        return roomService.save(room);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Room> findById(@PathVariable long id) {
        return roomService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<Room> findALl() {
        return roomService.findAll();
    }


    /**
     * Too fast integer.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/incrementTooFast/{id}")
    public Integer tooFast(@PathVariable long id) {
        return roomService.incrementTooFast(id);
    }

    /**
     * Too slow integer.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/incrementTooSlow/{id}")
    public Integer tooSlow(@PathVariable long id) {
        return roomService.incrementTooSlow(id);
    }

    /**
     * Gets the too fast count.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/getTooFast/{id}")
    public Integer getTooFast(@PathVariable long id) {
        return roomService.getTooFast(id);
    }

    /**
     * Sets the too fast count.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/setTooFast/{id}")
    public Integer setTooFast(@PathVariable long id) {
        return roomService.resetTooFast(id);
    }


    /**
     * Gets the too slow count.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/getTooSlow/{id}")
    public Integer getTooSlow(@PathVariable long id) {
        return roomService.getTooSlow(id);
    }

    /**
     * Sets the too slow count.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/setTooSlow/{id}")
    public Integer setTooSlow(@PathVariable long id) {
        return roomService.resetTooSlow(id);
    }

    /**
     * Increases the student count integer.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/increaseStudentCount/{id}")
    public Integer increaseStudentCount(@PathVariable long id) {
        return roomService.incrementStudentCount(id);
    }

    /**
     * Decreases the student count integer.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/decreaseStudentCount/{id}")
    public Integer decreaseStudentCount(@PathVariable long id) {
        return roomService.decrementStudentCount(id);
    }

    /**
     * Gets the student count.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/getStudentCount/{id}")
    public Integer getStudentCount(@PathVariable long id) {
        return roomService.getStudentCount(id);
    }

    /**
     * Join lecture string.
     *
     * @param roomCode the room code
     * @param name     the name
     * @param request  the request
     * @return the string
     */
    @GetMapping("/join")
    public User joinLecture(@RequestParam(name = "code") String roomCode,
                            @RequestParam(name = "name") String name,
                            HttpServletRequest request) {
        return roomService.joinLecture(roomCode, name, request);
    }

    /**
     * Close room room.
     *
     * @param id the id
     * @return the room
     */
    @GetMapping("/close/{id}")
    public Room closeRoom(@PathVariable long id) {
        return roomService.closeRoom(id);
    }

    /**
     * Reopen room for mod lect room.
     *
     * @param id the id
     * @return the room
     */
    @GetMapping("/reopenForModLect/{id}")
    public Room reopenRoomForModLect(@PathVariable long id) {
        return roomService.reopenRoomForModLect(id);
    }
}
