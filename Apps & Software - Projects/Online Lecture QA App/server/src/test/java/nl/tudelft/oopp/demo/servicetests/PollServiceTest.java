package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import nl.tudelft.oopp.demo.services.PollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * The type Poll service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
class PollServiceTest {

    @Autowired
    private PollService pollService;

    @MockBean
    private PollRepository pollRepository;

    private Poll poll1;
    private List<Poll> pollList;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        ArrayList<String> answers = new ArrayList<>();
        answers.add("choice 1");
        answers.add("choice 2");
        poll1 = new Poll(15, answers,"question text",1,true);
        poll1.setId(20);

        pollList = new ArrayList<Poll>();
        for (int i = 1; i < 10; i++) {
            Poll p = new Poll(i + 1, answers, "Question " + i,1,true);
            p.setId(i);
            pollList.add(p);
        }
    }

    /**
     * Save.
     */
    @Test
    void save() {
        when(pollRepository.save(poll1)).thenReturn(poll1);
        assertEquals(poll1, pollService.save(poll1));
    }

    /**
     * Find by id.
     */
    @Test
    void findById() {
        when(pollRepository.findById(20L)).thenReturn(java.util.Optional.of(poll1));
        assertEquals(Optional.of(poll1), pollService.findById(20L));
    }

    /**
     * Delete by id.
     */
    @Test
    void deleteById() {
        when(pollRepository.findAllById(List.of(20L))).thenReturn(List.of(poll1));
        pollService.deleteById(20L);
        verify(pollRepository, times(1)).deleteAll(List.of(poll1));
    }

    /**
     * Find all by lecture id.
     */
    @Test
    void findAllByLectureId() {
        pollList.stream().filter(p -> p.getId() < 5).forEach(q -> q.setLectureId(1));
        List<Poll> ret = pollList.stream()
                .filter(p -> p.getLectureId() == 1).collect(Collectors.toList());
        when(pollRepository.findAllByLectureIdEquals(1)).thenReturn(ret);
        assertEquals(pollList.subList(0,4), pollService.findAllByLectureId(1));
    }

    /**
     * Add vote to poll.
     */
    @Test
    void addVoteToPoll() {
        poll1.addVote(1);
        when(pollRepository.findById(15L)).thenReturn(Optional.ofNullable(poll1));
        assertEquals(pollService.addVoteToPoll(15, 1),2);

        when(pollRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(pollService.addVoteToPoll(60L,2));
    }

    /**
     * Close poll.
     */
    @Test
    void closePoll() {
        when(pollRepository.findById(15L)).thenReturn(Optional.ofNullable(poll1));
        poll1.setOpen(false);
        assertEquals(pollService.closePoll(15),poll1);

        when(pollRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(pollService.closePoll(60L));
    }

    /**
     * Open poll.
     */
    @Test
    void openPoll() {
        poll1.setOpen(false);
        when(pollRepository.findById(15L)).thenReturn(Optional.ofNullable(poll1));
        poll1.setOpen(true);
        assertEquals(pollService.openPoll(15),poll1);

        when(pollRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(pollService.openPoll(60L));
    }

    /**
     * Find all.
     */
    @Test
    void findAll() {
        when(pollRepository.findAll()).thenReturn(pollList);
        assertEquals(pollList, pollService.findAll());
    }
}