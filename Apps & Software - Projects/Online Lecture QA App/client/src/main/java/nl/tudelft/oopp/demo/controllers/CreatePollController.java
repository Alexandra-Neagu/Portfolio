package nl.tudelft.oopp.demo.controllers;

import java.net.URL;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.PollCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Poll;

/**
 * The type Create poll controller.
 */
public class CreatePollController {
    /**
     * The Question text box.
     */
    @FXML
    TextField questionTextBoxPoll;

    /**
     * The Create Poll button.
     */
    @FXML
    Button createPoll;

    /**
     * The cancel poll button.
     */
    @FXML
    Button cancel;

    /**
     * The Number of possible answers for polls.
     */
    @FXML
    MenuButton nmbOfAnswers;

    /**
     * The answer 1 text box.
     */
    @FXML
    TextField pollAnswerOption1;

    /**
     * The answer 1 correct check box.
     */
    @FXML
    CheckBox correctAnswer1;

    /**
     * The answer 2 text box.
     */
    @FXML
    TextField pollAnswerOption2;

    /**
     * The answer 2 correct check box.
     */
    @FXML
    CheckBox correctAnswer2;

    /**
     * The answer 3 text box.
     */
    @FXML
    TextField pollAnswerOption3;

    /**
     * The answer 3 correct check box.
     */
    @FXML
    CheckBox correctAnswer3;

    /**
     * The answer 4 text box.
     */
    @FXML
    TextField pollAnswerOption4;

    /**
     * The answer 4 correct check box.
     */
    @FXML
    CheckBox correctAnswer4;

    /**
     * The answer 5 text box.
     */
    @FXML
    TextField pollAnswerOption5;

    /**
     * The answer 5 correct check box.
     */
    @FXML
    CheckBox correctAnswer5;

    /**
     * The answer 6 text box.
     */
    @FXML
    TextField pollAnswerOption6;

    /**
     * The answer 6 correct check box.
     */
    @FXML
    CheckBox correctAnswer6;

    /**
     * The answer 7 text box.
     */
    @FXML
    TextField pollAnswerOption7;

    /**
     * The answer 7 correct check box.
     */
    @FXML
    CheckBox correctAnswer7;

    /**
     * The answer 8 text box.
     */
    @FXML
    TextField pollAnswerOption8;

    /**
     * The answer 8 correct check box.
     */
    @FXML
    CheckBox correctAnswer8;

    /**
     * The answer 9 text box.
     */
    @FXML
    TextField pollAnswerOption9;

    /**
     * The answer 9 correct check box.
     */
    @FXML
    CheckBox correctAnswer9;

    /**
     * The answer 10 text box.
     */
    @FXML
    TextField pollAnswerOption10;

    /**
     * The answer 10 correct check box.
     */
    @FXML
    CheckBox correctAnswer10;

    /**
     * The share result button.
     */
    @FXML
    CheckBox shareResults;

    /**
     * The Set quiz.
     */
    @FXML
    CheckBox setQuiz;

    /**
     * The Share result.
     */
    boolean shareResult = false;
    /**
     * The Correct question number.
     */
    Integer correctQuestionNumber = null;
    /**
     * The Selected options.
     */
    int selectedOptions = 0;
    /**
     * The Options.
     */
    ArrayList<String> options = new ArrayList<>(10);

    /**
     * checks how many options the creator wants in their poll
     * and creates them/shows them.
     *
     * @param event when selected makes the amount of questions appear
     */
    public void handleCreateAnswers(ActionEvent event) {
        MenuItem currentItem = (MenuItem) event.getSource();

        String amount = currentItem.getId();

        createPollAnswers(amount);
    }

    /**
     * Returns the amount of poll answer options dependent on the amount selected in the menu list.
     *
     * @param id the id of the menu item
     */
    public void createPollAnswers(String id) {
        final String two = "2";
        final String three = "3";
        final String four = "4";
        final String five = "5";
        final String six = "6";
        final String seven = "7";
        final String eight = "8";
        final String nine = "9";
        final String ten = "10";
        switch (id) {
            case "two":
                changeAmountPollAnswers(two);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(false);
                correctAnswer3.setVisible(false);
                pollAnswerOption4.setVisible(false);
                correctAnswer4.setVisible(false);
                pollAnswerOption5.setVisible(false);
                correctAnswer5.setVisible(false);
                pollAnswerOption6.setVisible(false);
                correctAnswer6.setVisible(false);
                pollAnswerOption7.setVisible(false);
                correctAnswer7.setVisible(false);
                pollAnswerOption8.setVisible(false);
                correctAnswer8.setVisible(false);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 2;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                }

                break;
            case "three":
                changeAmountPollAnswers(three);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(false);
                correctAnswer4.setVisible(false);
                pollAnswerOption5.setVisible(false);
                correctAnswer5.setVisible(false);
                pollAnswerOption6.setVisible(false);
                correctAnswer6.setVisible(false);
                pollAnswerOption7.setVisible(false);
                correctAnswer7.setVisible(false);
                pollAnswerOption8.setVisible(false);
                correctAnswer8.setVisible(false);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 3;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                }
                break;
            case "four":
                changeAmountPollAnswers(four);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(false);
                correctAnswer5.setVisible(false);
                pollAnswerOption6.setVisible(false);
                correctAnswer6.setVisible(false);
                pollAnswerOption7.setVisible(false);
                correctAnswer7.setVisible(false);
                pollAnswerOption8.setVisible(false);
                correctAnswer8.setVisible(false);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 4;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                }
                break;
            case "five":
                changeAmountPollAnswers(five);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(true);
                correctAnswer5.setVisible(true);
                pollAnswerOption6.setVisible(false);
                correctAnswer6.setVisible(false);
                pollAnswerOption7.setVisible(false);
                correctAnswer7.setVisible(false);
                pollAnswerOption8.setVisible(false);
                correctAnswer8.setVisible(false);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 5;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                    correctAnswer5.setVisible(false);
                }
                break;
            case "six":
                changeAmountPollAnswers(six);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(true);
                correctAnswer5.setVisible(true);
                pollAnswerOption6.setVisible(true);
                correctAnswer6.setVisible(true);
                pollAnswerOption7.setVisible(false);
                correctAnswer7.setVisible(false);
                pollAnswerOption8.setVisible(false);
                correctAnswer8.setVisible(false);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 6;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                    correctAnswer5.setVisible(false);
                    correctAnswer6.setVisible(false);
                }
                break;
            case "seven":
                changeAmountPollAnswers(seven);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(true);
                correctAnswer5.setVisible(true);
                pollAnswerOption6.setVisible(true);
                correctAnswer6.setVisible(true);
                pollAnswerOption7.setVisible(true);
                correctAnswer7.setVisible(true);
                pollAnswerOption8.setVisible(false);
                correctAnswer8.setVisible(false);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 7;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                    correctAnswer5.setVisible(false);
                    correctAnswer6.setVisible(false);
                    correctAnswer7.setVisible(false);
                }
                break;
            case "eight":
                changeAmountPollAnswers(eight);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(true);
                correctAnswer5.setVisible(true);
                pollAnswerOption6.setVisible(true);
                correctAnswer6.setVisible(true);
                pollAnswerOption7.setVisible(true);
                correctAnswer7.setVisible(true);
                pollAnswerOption8.setVisible(true);
                correctAnswer8.setVisible(true);
                pollAnswerOption9.setVisible(false);
                correctAnswer9.setVisible(false);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 8;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                    correctAnswer5.setVisible(false);
                    correctAnswer6.setVisible(false);
                    correctAnswer7.setVisible(false);
                    correctAnswer8.setVisible(false);
                }
                break;
            case "nine":
                changeAmountPollAnswers(nine);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(true);
                correctAnswer5.setVisible(true);
                pollAnswerOption6.setVisible(true);
                correctAnswer6.setVisible(true);
                pollAnswerOption7.setVisible(true);
                correctAnswer7.setVisible(true);
                pollAnswerOption8.setVisible(true);
                correctAnswer8.setVisible(true);
                pollAnswerOption9.setVisible(true);
                correctAnswer9.setVisible(true);
                pollAnswerOption10.setVisible(false);
                correctAnswer10.setVisible(false);
                selectedOptions = 9;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                    correctAnswer5.setVisible(false);
                    correctAnswer6.setVisible(false);
                    correctAnswer7.setVisible(false);
                    correctAnswer8.setVisible(false);
                    correctAnswer9.setVisible(false);
                }
                break;
            case "ten":
                changeAmountPollAnswers(ten);
                pollAnswerOption1.setVisible(true);
                correctAnswer1.setVisible(true);
                pollAnswerOption2.setVisible(true);
                correctAnswer2.setVisible(true);
                pollAnswerOption3.setVisible(true);
                correctAnswer3.setVisible(true);
                pollAnswerOption4.setVisible(true);
                correctAnswer4.setVisible(true);
                pollAnswerOption5.setVisible(true);
                correctAnswer5.setVisible(true);
                pollAnswerOption6.setVisible(true);
                correctAnswer6.setVisible(true);
                pollAnswerOption7.setVisible(true);
                correctAnswer7.setVisible(true);
                pollAnswerOption8.setVisible(true);
                correctAnswer8.setVisible(true);
                pollAnswerOption9.setVisible(true);
                correctAnswer9.setVisible(true);
                pollAnswerOption10.setVisible(true);
                correctAnswer10.setVisible(true);
                selectedOptions = 10;
                if (setQuiz.isSelected()) {
                    correctAnswer1.setVisible(false);
                    correctAnswer2.setVisible(false);
                    correctAnswer3.setVisible(false);
                    correctAnswer4.setVisible(false);
                    correctAnswer5.setVisible(false);
                    correctAnswer6.setVisible(false);
                    correctAnswer7.setVisible(false);
                    correctAnswer8.setVisible(false);
                    correctAnswer9.setVisible(false);
                    correctAnswer10.setVisible(false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handle share results.
     *
     * @param event the event
     */
    public void handleShareResults(ActionEvent event) {
        shareResult = !shareResult;
    }

    /**
     * Changes the set poll options button to the amount you selected.
     *
     * @param amountPollAnswers the text of the button
     */
    public void changeAmountPollAnswers(String amountPollAnswers) {
        nmbOfAnswers.setText(amountPollAnswers);
    }

    /**
     * Creates a poll and sets you back to the main screen.
     *
     * @throws Exception the exception
     */
    public void handleCreatePollBtn() throws Exception {
        String question = questionTextBoxPoll.getText();

        switch (selectedOptions) {
            case 2:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                break;
            case 3:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                break;
            case 4:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                break;
            case 5:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                options.add(4, pollAnswerOption5.getText());
                break;
            case 6:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                options.add(4, pollAnswerOption5.getText());
                options.add(5, pollAnswerOption6.getText());
                break;
            case 7:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                options.add(4, pollAnswerOption5.getText());
                options.add(5, pollAnswerOption6.getText());
                options.add(6, pollAnswerOption7.getText());
                break;
            case 8:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                options.add(4, pollAnswerOption5.getText());
                options.add(5, pollAnswerOption6.getText());
                options.add(6, pollAnswerOption7.getText());
                options.add(7, pollAnswerOption8.getText());
                break;
            case 9:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                options.add(4, pollAnswerOption5.getText());
                options.add(5, pollAnswerOption6.getText());
                options.add(6, pollAnswerOption7.getText());
                options.add(7, pollAnswerOption8.getText());
                options.add(8, pollAnswerOption9.getText());
                break;
            case 10:
                options.add(0, pollAnswerOption1.getText());
                options.add(1, pollAnswerOption2.getText());
                options.add(2, pollAnswerOption3.getText());
                options.add(3, pollAnswerOption4.getText());
                options.add(4, pollAnswerOption5.getText());
                options.add(5, pollAnswerOption6.getText());
                options.add(6, pollAnswerOption7.getText());
                options.add(7, pollAnswerOption8.getText());
                options.add(8, pollAnswerOption9.getText());
                options.add(9, pollAnswerOption10.getText());
                break;
            default:
                break;
        }

        Poll poll = new Poll(MainApp.getCurrentRoom().getId(),
                options, question, correctQuestionNumber - 1, shareResult);
        poll.setOpen(true);
        poll.setSharing(shareResult);
        PollCommunication.createPoll(poll);
        EventLog.logCreatePoll(MainApp.getCurrentUser(), poll, MainApp.getCurrentRoom());
        handleCancelPoll();
    }

    /**
     * Handle cancel poll.
     *
     * @throws Exception the exception
     */
    public void handleCancelPoll() throws Exception {
        switch (MainApp.getCurrentUser().getClass().getSimpleName()) {
            case "Lecturer":
                FXMLLoader loader = new FXMLLoader();
                URL xmlUrl = getClass().getResource("/LecturerMainPage.fxml");
                loader.setLocation(xmlUrl);
                Parent root = loader.load();
                Stage window = (Stage) createPoll.getScene().getWindow();
                window.setScene(new Scene(root));
                LecturerMainPageController.timeline.play();
                break;
            case "Moderator":
                FXMLLoader loader2 = new FXMLLoader();
                URL xmlUrl2 = getClass().getResource("/ModeratorMainPage.fxml");
                loader2.setLocation(xmlUrl2);
                Parent root2 = loader2.load();
                Stage window2 = (Stage) createPoll.getScene().getWindow();
                window2.setScene(new Scene(root2));
                ModeratorMainPageController.timeline.play();
                break;
            default:
                break;
        }
    }

    /**
     * Make quiz.
     *
     * @param event the event
     */
    public void makeQuiz(ActionEvent event) {

        correctQuestionNumber = 0;

        switch (selectedOptions) {
            case 10:
                correctAnswer10.setVisible(!correctAnswer10.isVisible());
                correctAnswer10.setSelected(false);
                correctAnswer9.setVisible(!correctAnswer9.isVisible());
                correctAnswer9.setSelected(false);
                correctAnswer8.setVisible(!correctAnswer8.isVisible());
                correctAnswer8.setSelected(false);
                correctAnswer7.setVisible(!correctAnswer7.isVisible());
                correctAnswer7.setSelected(false);
                correctAnswer6.setVisible(!correctAnswer6.isVisible());
                correctAnswer6.setSelected(false);
                correctAnswer5.setVisible(!correctAnswer5.isVisible());
                correctAnswer5.setSelected(false);
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 9:
                correctAnswer9.setVisible(!correctAnswer9.isVisible());
                correctAnswer9.setSelected(false);
                correctAnswer8.setVisible(!correctAnswer8.isVisible());
                correctAnswer8.setSelected(false);
                correctAnswer7.setVisible(!correctAnswer7.isVisible());
                correctAnswer7.setSelected(false);
                correctAnswer6.setVisible(!correctAnswer6.isVisible());
                correctAnswer6.setSelected(false);
                correctAnswer5.setVisible(!correctAnswer5.isVisible());
                correctAnswer5.setSelected(false);
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 8:
                correctAnswer8.setVisible(!correctAnswer8.isVisible());
                correctAnswer8.setSelected(false);
                correctAnswer7.setVisible(!correctAnswer7.isVisible());
                correctAnswer7.setSelected(false);
                correctAnswer6.setVisible(!correctAnswer6.isVisible());
                correctAnswer6.setSelected(false);
                correctAnswer5.setVisible(!correctAnswer5.isVisible());
                correctAnswer5.setSelected(false);
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 7:
                correctAnswer7.setVisible(!correctAnswer7.isVisible());
                correctAnswer7.setSelected(false);
                correctAnswer6.setVisible(!correctAnswer6.isVisible());
                correctAnswer6.setSelected(false);
                correctAnswer5.setVisible(!correctAnswer5.isVisible());
                correctAnswer5.setSelected(false);
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 6:
                correctAnswer6.setVisible(!correctAnswer6.isVisible());
                correctAnswer6.setSelected(false);
                correctAnswer5.setVisible(!correctAnswer5.isVisible());
                correctAnswer5.setSelected(false);
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 5:
                correctAnswer5.setVisible(!correctAnswer5.isVisible());
                correctAnswer5.setSelected(false);
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 4:
                correctAnswer4.setVisible(!correctAnswer4.isVisible());
                correctAnswer4.setSelected(false);
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 3:
                correctAnswer3.setVisible(!correctAnswer3.isVisible());
                correctAnswer3.setSelected(false);
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            case 2:
                correctAnswer2.setVisible(!correctAnswer2.isVisible());
                correctAnswer2.setSelected(false);
                correctAnswer1.setVisible(!correctAnswer1.isVisible());
                correctAnswer1.setSelected(false);
                break;
            default:
                break;
        }
    }

    /**
     * Registers and sets the answer that is considered correct.
     *
     * @param event when the checkbox is clicked
     */
    public void handleMarkCorrect(ActionEvent event) {
        CheckBox currentItem = (CheckBox) event.getSource();

        String correctQuestionId = currentItem.getId();

        switch (correctQuestionId) {
            case "correctAnswer1":
                correctQuestionNumber = 1;
                break;
            case "correctAnswer2":
                correctQuestionNumber = 2;
                break;
            case "correctAnswer3":
                correctQuestionNumber = 3;
                break;
            case "correctAnswer4":
                correctQuestionNumber = 4;
                break;
            case "correctAnswer5":
                correctQuestionNumber = 5;
                break;
            case "correctAnswer6":
                correctQuestionNumber = 6;
                break;
            case "correctAnswer7":
                correctQuestionNumber = 7;
                break;
            case "correctAnswer8":
                correctQuestionNumber = 8;
                break;
            case "correctAnswer9":
                correctQuestionNumber = 9;
                break;
            case "correctAnswer10":
                correctQuestionNumber = 10;
                break;
            default:
                correctQuestionNumber = -1;
                break;
        }
    }

}
