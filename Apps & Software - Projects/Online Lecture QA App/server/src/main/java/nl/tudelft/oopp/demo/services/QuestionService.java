package nl.tudelft.oopp.demo.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.StringResponse;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Question service.
 */
@Service
public class QuestionService {

    /**
     * The Question repository.
     */
    QuestionRepository questionRepository;

    /**
     * Instantiates a new Question service.
     *
     * @param questionRepository the question repository
     */
    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Save question.
     *
     * @param question the question
     * @return the question
     */
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    /**
     * Find all by room id list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> findAllByRoomId(long roomId) {
        return questionRepository.findAll().stream().filter(x -> x.getLectureId() == roomId)
                .collect(Collectors.toList());
    }

    /**
     * Find all answered list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> findAllAnswered(long roomId) {
        return questionRepository.selectAnswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Find all unanswered list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> findAllUnanswered(long roomId) {
        return questionRepository.selectNotAnswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(long id) {
        questionRepository.deleteAll(questionRepository.findAllById(List.of(id)));
    }


    /**
     * Upvoting a question.
     *
     * @param id question id
     * @return number of upvotes of the question
     */
    public Integer upvoteQuestion(long id) {
        if (this.findById(id).isPresent()) {
            Question q = this.findById(id).get();
            q.upvote();
            questionRepository.save(q);
            return q.getUpvotes();
        }
        return null;
    }

    /**
     * To mark a question as answered.
     *
     * @param id question id
     */
    public void markQuestionAnswered(long id) {
        if (this.findById(id).isPresent()) {
            Question q = this.findById(id).get();
            q.setAnswered(true);
            questionRepository.save(q);
        }
    }

    /**
     * Select by author id list.
     *
     * @param authorId the author id
     * @return the list
     */
    public List<Question> selectByAuthorId(long authorId)  {
        return questionRepository.selectByAuthorId(authorId);
    }


    /**
     * Sort by age list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> sortByAge(long roomId) {
        return questionRepository.sortByAge().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Unanswered questions sorted by age list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> sortByAgeUnanswered(long roomId) {
        return questionRepository.sortByAgeUnanswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Answered questions sorted by age list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> sortByAgeAnswered(long roomId) {
        return questionRepository.sortByAgeAnswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Select by upvotes list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> selectByUpvotes(long roomId)  {
        return questionRepository.selectByUpvotes().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Selects unanswered questions sorted by upvotes list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> selectByUpvotesUnanswered(long roomId)  {
        return questionRepository.selectByUpvotesUnanswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Selects answered questions sorted by upvotes list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> selectByUpvotesAnswered(long roomId)  {
        return questionRepository.selectByUpvotesAnswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
    }

    /**
     * Sort by popularity list.
     *
     * @param list the list
     * @return the list
     */
    public List<Question> sortByPopularity(List<Question> list)  {
        ArrayList<Long> popularity = new ArrayList<>(list.size());
        List<Question> sorted = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            long age = ChronoUnit.SECONDS
                        .between(list.get(i).getTimePublished(), LocalDateTime.now());

            // an upvote is worth 110 sec
            long popularityInt = ((long)110 * (long)list.get(i).getUpvotes() - age);
            popularity.add(popularityInt);
        }
        while (!popularity.isEmpty()) {
            int indexMax = 0;
            for (int i = 1; i < popularity.size(); i++) {
                if (popularity.get(i) > popularity.get(indexMax)) {
                    indexMax = i;
                }
            }
            sorted.add(list.get(indexMax));
            popularity.remove(indexMax);
            list.remove(indexMax);
        }
        return sorted;
    }

    /**
     * Select by popularity list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> selectByPopularity(long roomId)  {
        List<Question> list = findAllByRoomId(roomId);
        return sortByPopularity(list);
    }

    /**
     * Select by unanswered popularity list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> selectByPopularityUnanswered(long roomId)  {
        List<Question> list = questionRepository.selectByUpvotesUnanswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
        return sortByPopularity(list);
    }

    /**
     * Selects by answered popularity list.
     *
     * @param roomId the room id
     * @return the list
     */
    public List<Question> selectByPopularityAnswered(long roomId)  {
        List<Question> list = questionRepository.selectByUpvotesAnswered().stream()
                .filter(x -> x.getLectureId() == roomId).collect(Collectors.toList());
        return sortByPopularity(list);
    }

    /**
     * Modify the content of the question.
     *
     * @param id      the id
     * @param content the content
     * @return Question question
     */
    public StringResponse rephraseQuestion(long id, String content) {
        if (this.findById(id).isPresent()) {
            Question q = this.findById(id).get();
            q.setContent(content);
            questionRepository.save(q);
            return new StringResponse(content);
        }
        return null;
    }


    /**
     * Set an answerId for the question.
     *
     * @param id       the id
     * @param answerId the answer id
     * @return Question question
     */
    public Question setAnswerId(long id, long answerId) {
        if (this.findById(id).isPresent()) {
            Question q = this.findById(id).get();
            q.setAnswerId(answerId);
            questionRepository.save(q);
            return q;
        }
        return null;
    }
}