package nl.tudelft.oopp.demo.controllers;

import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.data.Room;

/**
 * The type Room code scene controller.
 */
public class RoomCodeSceneController {

    /**
     * The Course name.
     */
    @FXML
    public Label courseName;

    /**
     * The Lecture name.
     */
    @FXML
    public Label lectureName;

    /**
     * The Student room code label.
     */
    @FXML
    public Label studentRoomCodeLabel;

    /**
     * The Mod room code label.
     */
    @FXML
    public Label modRoomCodeLabel;

    /**
     * The Lecturer room code label.
     */
    @FXML
    public Label lecturerRoomCodeLabel;

    /**
     * The Starts at label.
     */
    @FXML
    public Label startsAtLabel;

    /**
     * The Back to menu btn.
     */
    @FXML
    public Button backToMenuBtn;
    /**
     * The Copy label student.
     */
    @FXML
    public Label copyLabelStudent;
    /**
     * The Copy label mod.
     */
    @FXML
    public Label copyLabelMod;
    /**
     * The Copy label lect.
     */
    @FXML
    public Label copyLabelLect;




    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            copyLabelStudent.setVisible(false);
            copyLabelMod.setVisible(false);
            copyLabelLect.setVisible(false);
            Room room = MainApp.getCurrentRoom();
            courseName.setText(room.getCourseName());
            lectureName.setText(room.getLectureName());
            studentRoomCodeLabel.setText("Student Room Code: " + room.getStudentRoomId());
            studentRoomCodeLabel.hoverProperty().addListener(l -> {
                copyLabelStudent.setVisible(true);
            });
            modRoomCodeLabel.hoverProperty().addListener(l -> {
                copyLabelMod.setVisible(true);
            });
            modRoomCodeLabel.hoverProperty().addListener(l -> {
                copyLabelLect.setVisible(true);
            });


            modRoomCodeLabel.setText("Moderator Room Code: " + room.getModeratorRoomId());
            lecturerRoomCodeLabel.setText("Lecturer Room Code: " + room.getLecturerRoomId());
            startsAtLabel.setText("Lecture will start on\n" + room.getScheduledDate().toString()
                    + " at " + room.getStartTime().toString());
        });
    }

    /**
     * Handle btn start lecture.
     *
     * @param actionEvent the action event
     */
    public void handleBtnStartLecture(ActionEvent actionEvent) {

    }

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

        Stage window = (Stage) backToMenuBtn.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    /**
     * Clicked student label.
     *
     * @param mouseEvent the mouse event
     */
    public void clickedStudentLabel(MouseEvent mouseEvent) {
        copyToClipboard(MainApp.getCurrentRoom().getStudentRoomId());
    }

    /**
     * Clicked mod label.
     *
     * @param mouseEvent the mouse event
     */
    public void clickedModLabel(MouseEvent mouseEvent) {
        copyToClipboard(MainApp.getCurrentRoom().getModeratorRoomId());
    }

    /**
     * Clicked lecturer label.
     *
     * @param mouseEvent the mouse event
     */
    public void clickedLecturerLabel(MouseEvent mouseEvent) {
        copyToClipboard(MainApp.getCurrentRoom().getLecturerRoomId());
    }

    /**
     * Copy to clipboard.
     *
     * @param text the text
     */
    public void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

}
