package nl.tudelft.oopp.demo.services;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.entities.Moderator;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * The type Room service.
 */
@Service
public class RoomService {

    private RoomRepository roomRepository;
    private StudentService studentService;
    private ModeratorService moderatorService;
    private LecturerService lecturerService;
    private BannedIpService bannedIpService;


    /**
     * Instantiates a new Room service.
     *
     * @param roomRepository   the room repository
     * @param studentService   the student service
     * @param moderatorService the moderator service
     * @param lecturerService  the lecturer service
     * @param bannedIpService  the banned ip service
     */
    @Autowired
    public RoomService(RoomRepository roomRepository,
                       StudentService studentService,
                       ModeratorService moderatorService,
                       LecturerService lecturerService,
                       BannedIpService bannedIpService) {

        this.roomRepository = roomRepository;
        this.studentService = studentService;
        this.moderatorService = moderatorService;
        this.lecturerService = lecturerService;
        this.bannedIpService = bannedIpService;
    }

    /**
     * Save room.
     *
     * @param room the room
     * @return the room
     */
    public Room save(Room room) {
        room.setStudentRoomId(generateRoomCode(5));
        room.setModeratorRoomId(generateRoomCode(7));
        room.setLecturerRoomId(generateRoomCode(9));
        return roomRepository.save(room);
    }

    /**
     * Generates a random alpha-numerical String room code with the given length.
     *
     * @param length the length
     * @return random String code
     */
    public static String generateRoomCode(int length) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Room> findById(long id) {
        return roomRepository.findById(id);
    }

    /**
     * Join lecture string.
     *
     * @param roomCode the room code
     * @param name     the name
     * @param request  the request
     * @return the string
     */
    public User joinLecture(String roomCode, String name, HttpServletRequest request) {

        // 400 = bad request, entered code is not a valid room code
        if (!checkIfCodeValid(roomCode)) {
            return null;
        }

        // 0 = student, 1 = moderator, 2 = lecturer
        int role = getClientRole(roomCode);

        List<Room> rooms = getRoomsByCode(roomCode);

        String clienIp = request.getRemoteAddr();

        List<Long> roomsBanned = bannedIpService.roomsWhereBanned(clienIp);

        String message = checkIfClientCanJoinRoom(rooms, roomsBanned);

        if (message.equals("200")) {

            User user = saveUserToDb(role, name, clienIp, rooms.get(0).getId());

            // if the code is valid,
            // send back to the client a message of the format "role+id"
            // e.g. "0+132" (Student with ID 132 in the DB),
            // e.g. "2+4" (Lecturer with ID 4 in the DB)
            message = role + "+" + user.getId();
            return user;
        }

        return null;
    }

    /**
     * Checks if a room code is valid (has an acceptable length).
     *
     * @param roomCode the room code
     * @return boolean result
     */
    public boolean checkIfCodeValid(String roomCode) {     //not private, for testing

        return roomCode.length() == Room.studentRoomCodeLength
                || roomCode.length() == Room.moderatorRoomCodeLength
                || roomCode.length() == Room.lecturerCodeLength;
    }

    /**
     * Saves a user in the correct table and returns it.
     *
     * @param role     the role
     * @param name     the name
     * @param clientIp the client ip
     * @param roomId   the room id
     * @return the user
     */
    public User saveUserToDb(int role, String name, String clientIp, long roomId) {

        User user;

        switch (role) {
            case 0:
            default:
                user = new Student(name, clientIp, roomId, LocalDateTime.now());
                studentService.save((Student) (user));
                break;
            case 1:
                user = new Moderator(name, clientIp, roomId, LocalDateTime.now());
                moderatorService.save((Moderator) (user));
                break;
            case 2:
                user = new Lecturer(name, clientIp, roomId, LocalDateTime.now());
                lecturerService.save((Lecturer) (user));
                break;
        }

        return user;
    }

    /**
     * Returns a list of all the rooms with that room code.
     *
     * @param roomCode the room code
     * @return list of rooms
     */
    public List<Room> getRoomsByCode(String roomCode) {
        switch (roomCode.length()) {
            case Room.studentRoomCodeLength:
            default:
                return roomRepository.findRoomByStudentRoomId(roomCode);
            case Room.moderatorRoomCodeLength:
                return roomRepository.findRoomByModeratorRoomId(roomCode);
            case Room.lecturerCodeLength:
                return roomRepository.findRoomByLecturerRoomId(roomCode);
        }
    }

    /**
     * Returns the role of the client (stud/mod/lect).
     *
     * @param roomCode the room code
     * @return integer role
     */
    public int getClientRole(String roomCode) {

        switch (roomCode.length()) {
            case Room.studentRoomCodeLength:
            default:
                return 0;
            case Room.moderatorRoomCodeLength:
                return 1;
            case Room.lecturerCodeLength:
                return 2;
        }
    }

    /**
     * Returns the status of the client's attempt at joining a lecture.
     *
     * @param rooms       the room (hopefully only one) the client tries to join
     * @param roomsBanned the rooms from which the client is banned
     * @return String status
     */
    public String checkIfClientCanJoinRoom(List<Room> rooms, List<Long> roomsBanned) {

        // 200 = lecture exists, is open rn, and you are allowed to enter it
        // 403 = you're banned from entering this lecture
        // 404 = lecture with this code doesn't exist
        // 500 = two rooms with the same code encountered, need to check for uniqueness
        // 503 = lecture exists, and you are allowed to enter, but it hasn't started yet

        if (rooms.size() > 1) {
            return "500";
        }

        if (rooms.isEmpty()) {
            return "404";
        } else {
            Room room = rooms.get(0);
            for (Long roomId : roomsBanned) {
                if (room.getId() == roomId) {
                    return "403";
                }
            }
            LocalDate ld = LocalDate.now();
            if (ld.isBefore(room.getScheduledDate())) {
                return "503";
            } else if (ld.isEqual(room.getScheduledDate())) {
                LocalTime lt = LocalTime.now();
                if (lt.isBefore(room.getStartTime())) {
                    return "503";
                } else {
                    return "200";
                }
            }
        }
        return "404";
    }

    /**
     * Increment too fast integer.
     *
     * @param id the id
     * @return the integer
     */
    public Integer incrementTooFast(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            r.incrementTooFastCount();
            roomRepository.save(r);
            return r.getTooFastCount();

        }
        return null;
    }

    /**
     * Increment too slow integer.
     *
     * @param id the id
     * @return the integer
     */
    public Integer incrementTooSlow(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            r.incrementTooSlowCount();
            roomRepository.save(r);
            return r.getTooSlowCount();

        }
        return null;
    }

    /**
     * Gets the too fast count.
     *
     * @param id the id
     * @return the fast count
     */
    public Integer getTooFast(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            roomRepository.save(r);
            return r.getTooFastCount();

        }
        return null;
    }

    /**
     * Resets the too fast count.
     *
     * @param id the id
     * @return the fast count
     */
    public Integer resetTooFast(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            r.setTooFastCount(0);
            roomRepository.save(r);
            return r.getTooFastCount();

        }
        return null;
    }


    /**
     * Gets the too slow count.
     *
     * @param id the id
     * @return the too slow count
     */
    public Integer getTooSlow(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            roomRepository.save(r);
            return r.getTooSlowCount();

        }
        return null;
    }

    /**
     * Resets the too slow count.
     *
     * @param id the id
     * @return the fast count
     */
    public Integer resetTooSlow(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            r.setTooSlowCount(0);
            roomRepository.save(r);
            return r.getTooSlowCount();

        }
        return null;
    }

    /**
     * Increments the student count.
     *
     * @param id the id
     * @return the student count
     */
    public Integer incrementStudentCount(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            r.increaseStudentCount();
            roomRepository.save(r);
            return r.getStudentCount();

        }
        return null;
    }

    /**
     * Decrements the student count.
     *
     * @param id the id
     * @return the student count
     */
    public Integer decrementStudentCount(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            r.decreaseStudentCount();
            roomRepository.save(r);
            return r.getStudentCount();

        }
        return null;
    }

    /**
     * Gets the student count.
     *
     * @param id the id
     * @return the student count
     */
    public Integer getStudentCount(long id) {
        if (this.findById(id).isPresent()) {
            Room r = this.findById(id).get();
            roomRepository.save(r);
            return r.getStudentCount();

        }
        return null;
    }

    /**
     * Close room room.
     *
     * @param id the id
     * @return the room
     */
    public Room closeRoom(long id) {
        if (this.findById(id).isPresent()) {
            Room room = this.findById(id).get();
            room.setHasBeenClosed(true);
            room.setStudentRoomId(null);
            roomRepository.save(room);
            return room;
        }
        return null;
    }

    /**
     * Open room room.
     *
     * @param id the id
     * @return the room
     */
    public Room reopenRoomForModLect(long id) {
        if (this.findById(id).isPresent()) {
            Room room = this.findById(id).get();
            room.setHasBeenClosed(false);
            room.setStudentRoomId(null);
            roomRepository.save(room);
            return room;
        }
        return null;
    }

}
