package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.RoomCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.views.CreateRoomDisplay;
import nl.tudelft.oopp.demo.views.LecturerMainPageDisplay;
import nl.tudelft.oopp.demo.views.ModeratorMainPageDisplay;
import nl.tudelft.oopp.demo.views.StudentMainPageDisplay;

/**
 * The type Join lecture controller.
 */
public class JoinLectureController {

    /**
     * The Name.
     */
    @FXML
    TextField name;
    /**
     * The Room code.
     */
    @FXML
    TextField roomCode;
    /**
     * The Join lecture btn.
     */
    @FXML
    Button joinLectureBtn;
    /**
     * The Back btn.
     */
    @FXML
    Button backBtn;

    /**
     * Handle btn back.
     *
     * @throws Exception the exception
     */
    public void handleBtnBack() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/OpeningScreen.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    /**
     * Handle btn join lecture.
     *
     * @throws IOException the io exception
     */
    public void handleBtnJoinLecture() throws IOException {
        if (name.getText().length() == 0) {
            showWarningAlert("Make sure you entered your name.");
        } else {
            User user = null;
            switch (roomCode.getLength()) {
                case 5:
                    user = new Student(name.getText());
                    break;
                case 7:
                    user = new Moderator(name.getText());
                    break;
                case 9:
                    user = new Lecturer(name.getText());
                    break;
                case 0:
                    showWarningAlert("Make sure you entered the room code.");
                    break;
                default:
                    showWarningAlert("Make sure you entered the correct room code.");
            }
            if (user != null) {
                try {
                    user = UserCommunication.joinLecture(roomCode.getText(), user);
                    MainApp.setCurrentUser(user);
                    MainApp.setCurrentRoom(RoomCommunication.getRoom(user.getRoomId()));

                    if (user instanceof Student) {
                        StudentMainPageDisplay studentMainPageDisplay
                                = new StudentMainPageDisplay();
                        Stage window = (Stage) joinLectureBtn.getScene().getWindow();
                        studentMainPageDisplay.start(window);
                        window.show();
                        RoomCommunication.increaseStudentCount();

                    } else if (user instanceof Lecturer) {
                        LecturerMainPageDisplay lecturerMainPageDisplay
                                = new LecturerMainPageDisplay();
                        Stage window = (Stage) joinLectureBtn.getScene().getWindow();
                        lecturerMainPageDisplay.start(window);
                        window.show();
                    } else if (user instanceof Moderator) {
                        ModeratorMainPageDisplay moderatorMainPageDisplay
                                = new ModeratorMainPageDisplay();
                        Stage window = (Stage) joinLectureBtn.getScene().getWindow();
                        moderatorMainPageDisplay.start(window);
                        window.show();
                    }
                    EventLog.logJoins(MainApp.getCurrentUser(), MainApp.getCurrentRoom());

                } catch (Exception e) {
                    System.out.println(e);
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Couldn't join the room. Please enter a correct room code.",
                            ButtonType.OK);
                    alert.showAndWait();

                }
            }
        }
    }

    private void showWarningAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content,
                ButtonType.OK);
        alert.showAndWait();
    }
}

