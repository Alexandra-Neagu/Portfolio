package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.PollCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Poll;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.views.StudentMainPageDisplay;

/**
 * The type Student poll controller.
 */
public class StudentPollController implements Initializable {

    /**
     * The question for the poll.
     */
    @FXML
    Label pollQuestionBox;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button0;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button1;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button2;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button3;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button4;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button5;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button6;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button7;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button8;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button9;

    /**
     * The Create Poll button.
     */
    @FXML
    Button giveUp;

    private Timeline timeline;

    /**
     * Rewrites the buttons.
     */
    public void rewriteTheButtons() {
        timeline.play();
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        for (int i = 0; i < poll.size(); i++) {
            if (poll.get(i).getOpen()) {
                Poll store = poll.get(i);
                pollQuestionBox.setText(store.getQuestion());

                switch (store.getAnswers().size()) {
                    case 2:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        break;
                    case 3:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        break;
                    case 4:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        break;
                    case 5:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button4.setText(store.getAnswers().get(4));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        button4.setVisible(true);
                        break;
                    case 6:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button4.setText(store.getAnswers().get(4));
                        button5.setText(store.getAnswers().get(5));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        button4.setVisible(true);
                        button5.setVisible(true);
                        break;
                    case 7:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button4.setText(store.getAnswers().get(4));
                        button5.setText(store.getAnswers().get(5));
                        button6.setText(store.getAnswers().get(6));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        button4.setVisible(true);
                        button5.setVisible(true);
                        button6.setVisible(true);
                        break;
                    case 8:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button4.setText(store.getAnswers().get(4));
                        button5.setText(store.getAnswers().get(5));
                        button6.setText(store.getAnswers().get(6));
                        button7.setText(store.getAnswers().get(7));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        button4.setVisible(true);
                        button5.setVisible(true);
                        button6.setVisible(true);
                        button7.setVisible(true);
                        break;
                    case 9:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button4.setText(store.getAnswers().get(4));
                        button5.setText(store.getAnswers().get(5));
                        button6.setText(store.getAnswers().get(6));
                        button7.setText(store.getAnswers().get(7));
                        button8.setText(store.getAnswers().get(8));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        button4.setVisible(true);
                        button5.setVisible(true);
                        button6.setVisible(true);
                        button7.setVisible(true);
                        button8.setVisible(true);
                        break;
                    case 10:
                        button0.setText(store.getAnswers().get(0));
                        button1.setText(store.getAnswers().get(1));
                        button2.setText(store.getAnswers().get(2));
                        button3.setText(store.getAnswers().get(3));
                        button4.setText(store.getAnswers().get(4));
                        button5.setText(store.getAnswers().get(5));
                        button6.setText(store.getAnswers().get(6));
                        button7.setText(store.getAnswers().get(7));
                        button8.setText(store.getAnswers().get(8));
                        button9.setText(store.getAnswers().get(9));
                        button0.setVisible(true);
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button3.setVisible(true);
                        button4.setVisible(true);
                        button5.setVisible(true);
                        button6.setVisible(true);
                        button7.setVisible(true);
                        button8.setVisible(true);
                        button9.setVisible(true);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    /**
     * Handles the voting and returns user to the
     * correct screen afterwards.
     *
     * @param event when clicked
     * @throws IOException if necessary
     */
    public void handleVoting(ActionEvent event) throws IOException {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        Student current = (Student) MainApp.getCurrentUser();
        for (int i = 0; i < poll.size(); i++) {
            if (poll.get(i).getOpen()) {
                Poll store = poll.get(i);

                Button currentItem = (Button) event.getSource();
                String answerIndex = currentItem.getId();

                switch (answerIndex) {
                    case "button0":
                        PollCommunication.addVoteToPoll(store.getId(), 0);
                        current.setVotedAnswer(0);
                        break;
                    case "button1":
                        PollCommunication.addVoteToPoll(store.getId(), 1);
                        current.setVotedAnswer(1);
                        break;
                    case "button2":
                        PollCommunication.addVoteToPoll(store.getId(), 2);
                        current.setVotedAnswer(2);
                        break;
                    case "button3":
                        PollCommunication.addVoteToPoll(store.getId(), 3);
                        current.setVotedAnswer(3);
                        break;
                    case "button4":
                        PollCommunication.addVoteToPoll(store.getId(), 4);
                        current.setVotedAnswer(4);
                        break;
                    case "button5":
                        PollCommunication.addVoteToPoll(store.getId(), 5);
                        current.setVotedAnswer(5);
                        break;
                    case "button6":
                        PollCommunication.addVoteToPoll(store.getId(), 6);
                        current.setVotedAnswer(6);
                        break;
                    case "button7":
                        PollCommunication.addVoteToPoll(store.getId(), 7);
                        current.setVotedAnswer(7);
                        break;
                    case "button8":
                        PollCommunication.addVoteToPoll(store.getId(), 8);
                        current.setVotedAnswer(8);
                        break;
                    case "button9":
                        PollCommunication.addVoteToPoll(store.getId(), 9);
                        current.setVotedAnswer(9);
                        break;
                    default:
                        break;
                }
                current.getAttendedPolls().add(store.getId());
                EventLog.logVotePoll(MainApp.getCurrentUser(), store, current.getVotedAnswer(),
                        MainApp.getCurrentRoom());
                StudentMainPageDisplay studentMainPageDisplay = new StudentMainPageDisplay();
                Stage window = (Stage) button0.getScene().getWindow();
                studentMainPageDisplay.start(window);
                window.show();
                timeline.stop();
            }
        }
    }

    /**
     * Makes sure the student can go back if he or she does not
     * want to partake in the poll.
     *
     * @throws IOException if necessary
     */
    public void handlePollReturnButton() throws IOException {

        //Creates new alert to ask the user if they are sure.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        //If the user presses the 'yes' button, app will take the user to the main menu.
        if (alert.getResult() == ButtonType.YES) {
            Poll store = getCurrentPoll();
            StudentMainPageController.reStartTimeline();
            Student current = (Student) MainApp.getCurrentUser();
            current.getAttendedPolls().add(store.getId());
            StudentMainPageDisplay studentMainPageDisplay = new StudentMainPageDisplay();
            Stage window = (Stage) button0.getScene().getWindow();
            studentMainPageDisplay.start(window);
            window.show();
            timeline.stop();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //automatic question field reload
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            try {
                checkIfPollIsClosed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Gets the current poll.
     *
     * @return the current poll
     */
    public Poll getCurrentPoll() {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        for (Poll value : poll) {
            if (value.getOpen()) {
                return value;
            }
        }
        return null;
    }

    /**
     * Checks whether or not the current poll is closed.
     *
     * @throws IOException if necessary
     */
    public void checkIfPollIsClosed() throws IOException {
        if (getCurrentPoll() == null) {
            StudentMainPageDisplay studentMainPageDisplay = new StudentMainPageDisplay();
            Stage window = (Stage) giveUp.getScene().getWindow();
            studentMainPageDisplay.start(window);
            window.show();
            timeline.stop();
        }
    }
}
