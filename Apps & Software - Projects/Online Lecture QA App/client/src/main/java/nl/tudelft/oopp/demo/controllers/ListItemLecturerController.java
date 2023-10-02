package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.QuestionCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.views.LecturerMainPageDisplay;
import nl.tudelft.oopp.demo.views.StudentMainPageDisplay;


/**
 * The type List item student controller.
 */
public class ListItemLecturerController implements Initializable {

    /**
     * The Time text.
     */
    @FXML
    Label timeText;
    /**
     * The Num of upvotes.
     */
    @FXML
    Label numOfUpvotes;
    /**
     * The Question text.
     */
    @FXML
    Label questionText;
    /**
     * The Answered label.
     */
    @FXML
    Label answeredLabel;
    /**
     * The Author's ID label.
     */
    @FXML
    Label authorID;
    /**
     * The Delete btn.
     */
    @FXML
    ImageView deleteBtn;

    /**
     * The mark as answered button.
     */
    @FXML
    ImageView answeredBtn;

    private long id;

    /**
     * Fill question.
     *
     * @param question the question
     */
    public void fillQuestion(Question question) {
        id = question.getId();
        questionText.setText(question.getContent());
        timeText.setText(question.getTimePublished().toString().substring(11,16));
        numOfUpvotes.setText(String.valueOf(question.getUpvotes()));

        if (UserCommunication.getStudent(question.getAuthorId()) != null) {
            authorID.setText(UserCommunication.getStudent(question.getAuthorId()).getName());
        } else {
            authorID.setText("Banned Author");
        }

        if (question.isAnswered()) {
            answeredLabel.setText("(answered)");
            answeredBtn.setDisable(true);
            answeredBtn.setOpacity(0.25);
        }
    }

    /**
     * Handle delete btn.
     *
     * @param mouseEvent the mouse event
     * @throws IOException the io exception
     */
    public void handleDeleteBtn(MouseEvent mouseEvent) throws IOException {

        //Creates new alert to ask the user if they are sure.

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        //If the user presses the 'yes' button, the question will be deleted.
        if (alert.getResult() == ButtonType.YES) {
            EventLog.logDeleteQuestion(MainApp.getCurrentUser(),
                    QuestionCommunication.getQuestion(id), MainApp.getCurrentRoom());

            QuestionCommunication.deleteQuestion(id);

            LecturerMainPageDisplay lecturerMainPageDisplay = new LecturerMainPageDisplay();
            Stage window = (Stage) deleteBtn.getScene().getWindow();
            lecturerMainPageDisplay.start(window);
            window.show();
        }
    }

    /**
     * Marks the question as answered.
     *
     * @param event the event
     */
    public void handleMarkAsAnswered(MouseEvent event) {
        QuestionCommunication.markAsAnswered(id);
        answeredBtn.setDisable(true);
        answeredBtn.setOpacity(0.25);
        EventLog.logMarkAsAnswered(MainApp.getCurrentUser(),
                QuestionCommunication.getQuestion(id), MainApp.getCurrentRoom());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}