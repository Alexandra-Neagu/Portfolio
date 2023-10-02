package nl.tudelft.oopp.demo.repositorytests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * The type Room repository test.
 */
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestEntityManager
public class RoomRepositoryTest {

    /**
     * The Entity manager.
     */
    @Autowired
    TestEntityManager entityManager;

    /**
     * The Room repository.
     */
    @Autowired
    RoomRepository roomRepository;

    /**
     * Test find room by student room id.
     */
    @Test
    void testFindRoomByStudentRoomId() {
        Room room = new Room();
        room.setStudentRoomId("1");

        room = entityManager.persistAndFlush(room);
        assertEquals(room, roomRepository.findRoomByStudentRoomId(room.getStudentRoomId())
                .get(0));
    }

    /**
     * Test find room by moderator room id.
     */
    @Test
    void testFindRoomByModeratorRoomId() {
        Room room = new Room();
        room.setModeratorRoomId("1");

        room = entityManager.persistAndFlush(room);
        assertEquals(room,roomRepository.findRoomByModeratorRoomId(room.getModeratorRoomId())
                .get(0));
    }

    /**
     * Test find room by lecturer room id.
     */
    @Test
    void testFindRoomByLecturerRoomId() {
        Room room = new Room();
        room.setLecturerRoomId("1");

        room = entityManager.persistAndFlush(room);
        assertEquals(room,roomRepository.findRoomByLecturerRoomId(room.getLecturerRoomId())
                .get(0));
    }
}