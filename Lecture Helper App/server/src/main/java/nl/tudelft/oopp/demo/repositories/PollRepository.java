package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface Poll repository.
 */
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    /**
     * Find all by lecture id equals list.
     *
     * @param lectureId the lecture id
     * @return the list
     */
    @Query(value = "SELECT p FROM Poll p WHERE lectureId = ?1")
    List<Poll> findAllByLectureIdEquals(long lectureId);
}
