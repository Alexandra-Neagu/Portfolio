package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface Room repository.
 */
@Repository("RoomRepository")
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Find room by student room id list.
     *
     * @param studentCode the student code
     * @return the list
     */
    // return type is list, but it should really return only 1 room,
    // since the joining code should be unique
    @Query(value = "SELECT r FROM Room r WHERE Stu_room_id = ?1")
    public List<Room> findRoomByStudentRoomId(String studentCode);

    /**
     * Find room by moderator room id list.
     *
     * @param moderatorCode the moderator code
     * @return the list
     */
    // return type is list, but it should really return only 1 room,
    // since the joining code should be unique
    @Query(value = "SELECT r FROM Room r WHERE Mod_room_id = ?1")
    public List<Room> findRoomByModeratorRoomId(String moderatorCode);

    /**
     * Find room by lecturer room id list.
     *
     * @param lecturerCode the lecturer code
     * @return the list
     */
    // return type is list, but it should really return only 1 room,
    // since the joining code should be unique
    @Query(value = "SELECT r FROM Room r WHERE Lect_room_id = ?1")
    public List<Room> findRoomByLecturerRoomId(String lecturerCode);
}
