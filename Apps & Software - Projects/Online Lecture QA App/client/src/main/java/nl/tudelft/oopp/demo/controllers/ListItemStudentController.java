package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.AnswerCommunication;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.QuestionCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.Answer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.views.StudentMainPageDisplay;


/**
 * The type List item student controller.
 */
public class ListItemStudentController implements Initializable {

    /**
     * The Hbox.
     */
    @FXML
    HBox hbox;
    /**
     * The Question item.
     */
    @FXML
    AnchorPane questionItem;
    /**
     * The Upvote btn.
     */
    @FXML
    ImageView upvoteBtn;

    /**
     * The Delete btn.
     */
    @FXML
    ImageView deleteBtn;
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
        if (question.isAnswered()) {
            answeredLabel.setText("(answered)");
        }
        if (question.getAuthorId() == MainApp.getCurrentUser().getId()) {
            upvoteBtn.setDisable(true);
            upvoteBtn.setOpacity(0.25);
            deleteBtn.setVisible(true);
        }
        Student current = (Student) MainApp.getCurrentUser();
        if (current.getUpvotedQuestions().contains(id)) {
            upvoteBtn.setDisable(true);
            upvoteBtn.setOpacity(0.25);
        }

        if (question.getAnswerId() != 0) {
            Answer answer = AnswerCommunication.getAnswer(question.getAnswerId());


            User user = null;
            //to get the name of the moderator/lecturer who wrote the answer from the id
            if (answer.getModeratorId() != 0) {
                user = UserCommunication.getModerator(answer.getModeratorId());
            } else {
                user = UserCommunication.getLecturer(answer.getLecturerId());
            }

            //java code to generate the necessary fxml code for the answer
            Label answerText = new Label(answer.getContent());
            answerText.setPrefWidth(400);
            Label modName = new Label(user.getName());
            Label timeText = new Label(answer.getTimePublished().toString().substring(11,16));
            VBox vbox = new VBox(modName,timeText);
            vbox.setPrefWidth(90);
            HBox hbox = new HBox(vbox, answerText);
            hbox.setPrefWidth(500);
            hbox.setLayoutX(4);
            AnchorPane answerPane = new AnchorPane(hbox);
            answerPane.setLayoutY(50);
            answerPane.setLayoutX(4);
            answerPane.setStyle("-fx-background-color: #caf3e6");
            questionItem.getChildren().add(answerPane);
        }
    }

    /**
     * Handle upvote btn.
     *
     * @param mouseEvent the mouse event
     */
    public void handleUpvoteBtn(MouseEvent mouseEvent) {
        QuestionCommunication.upvoteQuestion(id);
        numOfUpvotes.setText(String.valueOf(QuestionCommunication.getQuestion(id).getUpvotes()));
        upvoteBtn.setDisable(true);
        upvoteBtn.setOpacity(0.25);
        Student current = (Student) MainApp.getCurrentUser();
        current.getUpvotedQuestions().add(id);
        EventLog.logUpvoteQuestion(MainApp.getCurrentUser(),
                QuestionCommunication.getQuestion(id), MainApp.getCurrentRoom());
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

            StudentMainPageDisplay studentMainPageDisplay = new StudentMainPageDisplay();
            Stage window = (Stage) deleteBtn.getScene().getWindow();
            studentMainPageDisplay.start(window);
            window.show();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
