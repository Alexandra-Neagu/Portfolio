package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.RoomCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.views.OpeningScreenDisplay;
import nl.tudelft.oopp.demo.views.RoomCodeDisplay;

/**
 * The type Create room scene controller.
 */
public class CreateRoomSceneController {
    /**
     * The Course name.
     */
    public TextField courseName;
    /**
     * The Lecture name.
     */
    public TextField lectureName;
    /**
     * The Ends at.
     */
    public TextField endTime;
    /**
     * The Start time.
     */
    public TextField startTime;
    /**
     * The name of the lecturer.
     */
    public TextField profName;
    /**
     * The date of the lecture.
     */
    public DatePicker date;

    /**
     * The Cancel btn.
     */
    @FXML
    Button cancelBtn;

    /**
     * The Create room btn.
     */
    @FXML
    public Button createRoomBtn;


    /**
     * Handle btn cancel.
     *
     * @throws Exception the exception
     */
    public void handleBtnCancel() throws Exception {
        OpeningScreenDisplay openingScreenDisplay = new OpeningScreenDisplay();
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        openingScreenDisplay.start(window);
        window.show();
    }

    /**
     * Handle btn create room.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    public void handleBtnCreateRoom(ActionEvent event) throws IOException {
        if (checkIfNotEmpty()) {
            try {
                LocalTime startingAt = LocalTime.parse(startTime.getText());
                LocalTime endingAt = LocalTime.parse(endTime.getText());
                Room room = new Room(courseName.getText(),
                        lectureName.getText(),
                        date.getValue(),
                        startingAt,
                        endingAt);
                Lecturer lecturer = new Lecturer(profName.getText());
                MainApp.setCurrentRoom(RoomCommunication.createRoom(room));
                lecturer.setRoomId(MainApp.getCurrentRoom().getId());
                UserCommunication.createUser(lecturer);

                RoomCodeDisplay roomCodeDisplay = new RoomCodeDisplay();
                Stage window = (Stage) createRoomBtn.getScene().getWindow();
                roomCodeDisplay.start(window);
                window.show();
                EventLog.createEventLog(MainApp.getCurrentRoom());
                EventLog.logRoomCreated(MainApp.getCurrentRoom());
            } catch (Exception e) {
                if (e instanceof DateTimeParseException) {
                    showAlert("Please use the 00:00 format for start/end times!");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkIfNotEmpty() {
        if (courseName.getText().length() == 0) {
            showAlert("Make sure you entered the name of the course.");
        } else if (lectureName.getText().length() == 0) {
            showAlert("Make sure you entered the name of the lecture.");
        } else if (date.getValue() == null) {
            showAlert("Make sure you entered the date properly."); //***********
        } else if (startTime.getText().length() == 0) {
            showAlert("Make sure you entered the start time of the lecture.");
        } else if (endTime.getText().length() == 0) {
            showAlert("Make sure you entered the end time of the lecture.");
        } else if (profName.getText().length() == 0) {
            showAlert("Make sure you entered your name.");
        } else {
            return true;
        }
        return false;
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content,
                ButtonType.OK);
        alert.showAndWait();
    }
}
