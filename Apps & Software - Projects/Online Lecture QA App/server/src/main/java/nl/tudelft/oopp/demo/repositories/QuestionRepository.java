package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface Question repository.
 */
@Repository("QuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, Long> {

    //    @Modifying
    //    @Transactional
    //    @Query(value = "UPDATE Person p SET p.name = ?1 WHERE p.id = ?2")
    //    void updateNameById(String name, long id);

    /**
     * Select by author id list.
     *
     * @param authorId the author id
     * @return the list
     */
    @Query(value = "SELECT q FROM Question q WHERE authorId = ?1")
    List<Question> selectByAuthorId(long authorId);


    /**
     * Sort by age list.
     *
     * @return the list
     */
    @Query(value = "SELECT q FROM Question q ORDER BY timePublished DESC")
    List<Question> sortByAge();

    /**
     * Select by upvotes list.
     *
     * @return the list
     */
    @Query(value = "Select q FROM Question q ORDER BY upvotes DESC")
    List<Question> selectByUpvotes();

    /**
     * Select answered list.
     *
     * @return the list
     */
    @Query(value = "Select q from Question q where isAnswered = true")
    List<Question> selectAnswered();

    /**
     * Select not answered list.
     *
     * @return the list
     */
    @Query(value = "Select q from Question q where isAnswered = false")
    List<Question> selectNotAnswered();

    /**
     * Sort by age unanswered list.
     *
     * @return the list
     */
    @Query(value = "Select q from Question q where isAnswered = false ORDER BY timePublished DESC")
    List<Question> sortByAgeUnanswered();

    /**
     * Sort by age answered list.
     *
     * @return the list
     */
    @Query(value = "Select q from Question q where isAnswered = true ORDER BY timePublished DESC")
    List<Question> sortByAgeAnswered();

    /**
     * Select by upvotes unanswered list.
     *
     * @return the list
     */
    @Query(value = "Select q from Question q where isAnswered = false ORDER BY upvotes DESC")
    List<Question> selectByUpvotesUnanswered();

    /**
     * Select by upvotes answered list.
     *
     * @return the list
     */
    @Query(value = "Select q from Question q where isAnswered = true ORDER BY upvotes DESC")
    List<Question> selectByUpvotesAnswered();
}
