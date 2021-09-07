package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.entities.Moderator;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.StudentRepository;
import nl.tudelft.oopp.demo.services.RoomService;
import nl.tudelft.oopp.demo.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The type Room service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    private static List<Room> roomList;
    private static Room r1;

    /**
     * The S 1.
     */
    final String s1 = "12345";
    /**
     * The S 2.
     */
    final String s2 = "1234567";
    /**
     * The S 3.
     */
    final String s3 = "123456789";

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        r1 = new Room();
        r1.setId(1);
        r1.setScheduledDate(LocalDate.of(2000, 1, 1));
        r1.setLectureName("Git");
        r1.setCourseName("OOPP");
        r1.setStartTime(LocalTime.of(10, 30));
        r1.setEndTime(LocalTime.of(12, 0));
        r1.setHasBeenClosed(false);

        roomList = new ArrayList<Room>();
        for (int i = 1; i < 15; i++) {
            Room r = new Room();
            r.setId(1);
            r.setScheduledDate(LocalDate.of(2000, 1, i));
            r.setLectureName("Lecture " + i);
            r.setCourseName("Course " + i);
            r.setStartTime(LocalTime.of(10, i));
            r.setEndTime(LocalTime.of(12, i));
            r.setHasBeenClosed(false);
            roomList.add(r);
        }

    }

    /**
     * Save.
     */
    @Test
    void save() {
        when(roomRepository.save(r1)).thenReturn(r1);
        assertEquals(r1, roomService.save(r1));
    }

    /**
     * Increment too fast.
     */
    @Test
    void incrementTooFast() {
        r1.setTooFastCount(1);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.incrementTooFast(1), 2);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.incrementTooFast(60));
    }

    /**
     * Increment too slow.
     */
    @Test
    void incrementTooSlow() {
        r1.setTooSlowCount(2);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.incrementTooSlow(1), 3);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.incrementTooSlow(60));
    }

    /**
     * Gets too fast.
     */
    @Test
    void getTooFast() {
        r1.setTooFastCount(2);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.getTooFast(1), 2);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.getTooFast(60));
    }

    /**
     * Reset too fast.
     */
    @Test
    void resetTooFast() {
        r1.setTooFastCount(2);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.resetTooFast(1), 0);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.resetTooFast(60));
    }

    /**
     * Gets too slow.
     */
    @Test
    void getTooSlow() {
        r1.setTooSlowCount(1);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.getTooSlow(1), 1);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.getTooSlow(60));
    }

    /**
     * Reset too slow.
     */
    @Test
    void resetTooSlow() {
        r1.setTooSlowCount(2);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.resetTooSlow(1), 0);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.resetTooSlow(60));
    }

    /**
     * Increment student count.
     */
    @Test
    void incrementStudentCount() {
        //initially, the student count is 0;
        r1.increaseStudentCount();  //student count: 1
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        //after the method in roomService is called, it is expected to be incremented by 1
        assertEquals(roomService.incrementStudentCount(1), 2);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.incrementStudentCount(60));
    }

    /**
     * Decrement student count.
     */
    @Test
    void decrementStudentCount() {
        r1.increaseStudentCount();  //student count: 1
        r1.increaseStudentCount();  //student count: 2
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        //after the method in roomService is called, it is expected to be decreased by 1
        assertEquals(roomService.decrementStudentCount(1), 1);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.decrementStudentCount(60));
    }

    /**
     * Gets student count.
     */
    @Test
    void getStudentCount() {
        r1.increaseStudentCount();  //student count: 1
        r1.increaseStudentCount();  //student count: 2
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        assertEquals(roomService.getStudentCount(1), 2);

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.getStudentCount(60));
    }

    /**
     * Close room.
     */
    @Test
    void closeRoom() {
        r1.setStudentRoomId("exampleCode");
        r1.setHasBeenClosed(false);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        Room room = roomService.closeRoom(1);
        assertEquals(room.getHasBeenClosed(), true);
        assertNull(room.getStudentRoomId());

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.closeRoom(60));
    }

    /**
     * Reopen room for mod lect.
     */
    @Test
    void reopenRoomForModLect() {
        r1.setStudentRoomId("exampleCode");
        r1.setHasBeenClosed(true);
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(r1));
        Room room = roomService.reopenRoomForModLect(1);
        assertEquals(room.getHasBeenClosed(), false);
        assertNull(room.getStudentRoomId());

        when(roomRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(roomService.reopenRoomForModLect(60));
    }

    /**
     * Find all.
     */
    @Test
    void findAll() {
        when(roomRepository.findAll()).thenReturn(roomList);
        assertEquals(roomList, roomService.findAll());
    }

    /**
     * Find by id.
     */
    @Test
    void findById() {
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(r1));
        assertEquals(Optional.of(r1), roomService.findById(1));
    }

    /**
     * Generate room code.
     */
    @Test
    void generateRoomCode() {
        String code1 = roomService.generateRoomCode(5);
        String code2 = roomService.generateRoomCode(5);

        assertFalse(code1.equals(code2)); //to check if they are unique
        assertTrue(code1.length() == 5);
        assertTrue(code2.length() == 5);
    }

    /**
     * Check if code valid.
     */
    @Test
    void checkIfCodeValid() {
        assertTrue(roomService.checkIfCodeValid(s1));
        assertTrue(roomService.checkIfCodeValid(s2));
        assertTrue(roomService.checkIfCodeValid(s3));
        String s4 = "123";
        assertFalse(roomService.checkIfCodeValid(s4));
    }

    /**
     * Gets rooms by code.
     */
    @Test
    void getRoomsByCode() {
        roomService.getRoomsByCode(s1);
        verify(roomRepository, times(1)).findRoomByStudentRoomId(s1);

        roomService.getRoomsByCode(s2);
        verify(roomRepository, times(1)).findRoomByModeratorRoomId(s2);

        roomService.getRoomsByCode(s3);
        verify(roomRepository, times(1)).findRoomByLecturerRoomId(s3);
    }

    /**
     * Gets client role.
     */
    @Test
    void getClientRole() {
        assertEquals(roomService.getClientRole(s1), 0);
        assertEquals(roomService.getClientRole(s2), 1);
        assertEquals(roomService.getClientRole(s3), 2);
    }

    /**
     * Check if client can join room.
     */
    @Test
    void checkIfClientCanJoinRoom() {
        List<Room> emptyRoom = List.of();
        List<Room> room = List.of(r1);
        List<Long> roomsBanned = List.of(1L,2L);

        assertEquals(roomService.checkIfClientCanJoinRoom(emptyRoom, roomsBanned),"404");
        //check with a room List with more than 1 room in it
        assertEquals(roomService.checkIfClientCanJoinRoom(roomList, roomsBanned),"500");

        assertEquals(roomService.checkIfClientCanJoinRoom(room, roomsBanned),"403");

        Room r2 = new Room();
        r2.setId(10);
        r2.setScheduledDate(LocalDate.of(2022, 1, 1));
        r2.setStartTime(LocalTime.of(10, 30));
        r2.setEndTime(LocalTime.of(12, 0));
        List<Room> room2 = List.of(r2);

        assertEquals(roomService.checkIfClientCanJoinRoom(room2, roomsBanned),"503");

        r2.setStartTime(LocalTime.now());
        r2.setScheduledDate(LocalDate.now());
        assertEquals(roomService.checkIfClientCanJoinRoom(room2, roomsBanned),"200");

    }

    /**
     * Save user to db.
     */
    @Test
    void saveUserToDb() {
        assertTrue(roomService.saveUserToDb(0, "a", "ip", 1) instanceof Student);
        assertTrue(roomService.saveUserToDb(1, "b", "ip2", 1) instanceof Moderator);
        assertTrue(roomService.saveUserToDb(2, "c", "ip3", 1) instanceof Lecturer);

    }
}