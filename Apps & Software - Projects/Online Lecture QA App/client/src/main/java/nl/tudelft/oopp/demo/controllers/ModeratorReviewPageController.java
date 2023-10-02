package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.BannedIpCommunication;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.QuestionCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.BannedIp;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.views.LecturerMainPageDisplay;
import nl.tudelft.oopp.demo.views.ModeratorMainPageDisplay;


/**
 * The type Moderator review page controller.
 */
public class ModeratorReviewPageController implements Initializable {

    /**
     * The Question text.
     */
    @FXML
    Label questionText;

    /**
     * The Num of upvotes.
     */
    @FXML
    Label numOfUpvotes;

    /**
     * The Time text.
     */
    @FXML
    Label timeText;

    /**
     * The Student name.
     */
    @FXML
    Label studentName;

    /**
     * The Rephrase text field.
     */
    @FXML
    TextField rephraseTextField;

    /**
     * The Rephrase question btn.
     */
    @FXML
    Button rephraseQuestionBtn;

    /**
     * The Go back btn.
     */
    @FXML
    Button goBackBtn;

    /**
     * The Delete btn.
     */
    @FXML
    Button deleteBtn;

    private long id;

    /**
     * The User.
     */
    User user;

    /**
     * The Question.
     */
    Question question;

    /**
     * Fill the question field with the current question parameters.
     *
     * @param studentQuestion the student question
     */
    public void fillQuestion(Question studentQuestion) {
        question = studentQuestion;
        id = question.getId();
        questionText.setText(question.getContent());
        numOfUpvotes.setText(String.valueOf(question.getUpvotes()));
        timeText.setText(question.getTimePublished().toString().substring(11,16));
        user = UserCommunication.getStudent(question.getAuthorId());
        studentName.setText(user.getName());
    }

    /**
     * Change the content of the question if it is not deleted.
     */
    public void rephraseQuestion() {
        if (QuestionCommunication.getQuestion(question.getId()) == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "This question is deleted, you can't rephrase.",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            String newContent = rephraseTextField.getText();
            if (newContent.equals("") || newContent == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Please fill in the text field with the new content!",
                        ButtonType.OK);
                alert.showAndWait();
            } else {
                QuestionCommunication.rephraseQuestion(id, newContent);
                questionText.setText(QuestionCommunication.getQuestion(id).getContent());
                EventLog.logRephraseQuestion(MainApp.getCurrentUser(),
                        QuestionCommunication.getQuestion(id), MainApp.getCurrentRoom());
            }
        }
    }

    /**
     * Ban the student.
     */
    public void banStudent() {
        if (UserCommunication.getStudent(QuestionCommunication.getQuestion(id)
                .getAuthorId()) == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The student is already banned",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            //If the user presses the 'yes' button, app will take the user to the main menu.
            if (alert.getResult() == ButtonType.YES) {
                System.out.println(user.getIpAddress());
                EventLog.logBanStudent(MainApp.getCurrentUser(), user, MainApp.getCurrentRoom());
                BannedIp bannedIp = new BannedIp(user.getIpAddress(), LocalTime.now(),
                        MainApp.getCurrentRoom().getId());
                BannedIpCommunication.banIP(bannedIp);
                UserCommunication.deleteStudent(user.getId());
            }
        }

    }

    /**
     * Goes back to moderator main page.
     *
     * @throws IOException the io exception
     */
    public void handleGoBackBtn() throws IOException {
        if (MainApp.getCurrentUser() instanceof Moderator) {
            ModeratorMainPageDisplay moderatorMainPageDisplay = new ModeratorMainPageDisplay();
            Stage window = (Stage) goBackBtn.getScene().getWindow();
            moderatorMainPageDisplay.start(window);
            window.show();
            ModeratorMainPageController.timeline.play();
        }

        if (MainApp.getCurrentUser() instanceof Lecturer) {
            LecturerMainPageDisplay lecturerMainPageDisplay = new LecturerMainPageDisplay();
            Stage window = (Stage) goBackBtn.getScene().getWindow();
            lecturerMainPageDisplay.start(window);
            window.show();
            LecturerMainPageController.timeline.play();
        }

    }

    /**
     * If the question isn't already deleted, delete it.
     */
    public void handleDeleteBtn() {
        if (QuestionCommunication.getQuestion(question.getId()) == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "This question is already deleted",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you want to delete the question?",
                    ButtonType.YES, ButtonType.NO);
            alert2.showAndWait();
            if (alert2.getResult() == ButtonType.YES) {
                EventLog.logDeleteQuestion(MainApp.getCurrentUser(),
                        QuestionCommunication.getQuestion(id), MainApp.getCurrentRoom());
                QuestionCommunication.deleteQuestion(question.getId());
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION, "You deleted the question",
                        ButtonType.OK);
                alert3.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
