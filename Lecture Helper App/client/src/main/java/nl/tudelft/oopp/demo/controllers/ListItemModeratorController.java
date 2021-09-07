package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.Answer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.User;


/**
 * The type List item student controller.
 */
public class ListItemModeratorController implements Initializable {

    /**
     * The Upvote btn.
     */
    @FXML
    ImageView upvoteBtn;

    /**
     * The answer btn.
     */
    @FXML
    ImageView answerBtn;

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
     * The Question text box.
     */
    @FXML
    TextField replyTextField;

    /**
     * The Review btn.
     */
    @FXML
    Button reviewBtn;

    /**
     * The Question item.
     */
    @FXML
    AnchorPane questionItem;

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
            answerBtn.setDisable(true);
            answerBtn.setOpacity(0.25);
            //answeredLabel.setText("(answered)");
        }

        if (question.getAnswerId() != 0) {
            replyTextField.setPromptText("Change the answer here");
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
            answerPane.setLayoutY(70);
            answerPane.setLayoutX(4);
            answerPane.setStyle("-fx-background-color: #caf3e6");
            questionItem.getChildren().add(answerPane);
        }
    }

    /**
     * Handle answer btn.
     *
     * @param mouseEvent the mouse event
     */
    public void handleAnswerBtn(MouseEvent mouseEvent) {
        QuestionCommunication.markAsAnswered(id);
        answerBtn.setDisable(true);
        answerBtn.setOpacity(0.25);
        EventLog.logMarkAsAnswered(MainApp.getCurrentUser(),
                QuestionCommunication.getQuestion(id), MainApp.getCurrentRoom());
    }

    /**
     * Handle reply btn.
     */
    public void handleResponseBtn() {
        ModeratorMainPageController.timeline.play();
        String content = replyTextField.getText();
        if (content == null || content.length() < 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Make sure you fully entered your reply.",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            Answer answer = new Answer(content);
            answer.setModeratorId(MainApp.getCurrentUser().getId());
            answer = AnswerCommunication.postAnswer(answer);
            //System.out.println("Answer ID: " + answer.getId());
            //System.out.println("Answer TIME: " + answer.getTimePublished());

            QuestionCommunication.setAnswerId(id, answer.getId());
            QuestionCommunication.markAsAnswered(id);
            EventLog.logAnswer(MainApp.getCurrentUser(),
                    QuestionCommunication.getQuestion(id), answer, MainApp.getCurrentRoom());
            replyTextField.setPromptText("Change the answer here");
        }
    }

    /**
     * Opens the moderator review page and fills the question in the page.
     *
     * @throws IOException the io exception
     */
    public void handleReviewBtn() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/ModeratorReviewQuestionPage.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        ModeratorReviewPageController moderatorReviewPageController = loader.getController();
        moderatorReviewPageController.fillQuestion(QuestionCommunication.getQuestion(id));
        Stage window = (Stage) reviewBtn.getScene().getWindow();
        window.setScene(new Scene(root));

        if (MainApp.getCurrentUser() instanceof Moderator) {
            ModeratorMainPageController.timeline.stop();
        }
    }

    /**
     * Stop timeline.
     */
    public void stopTimeline() {
        ModeratorMainPageController.timeline.stop();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}