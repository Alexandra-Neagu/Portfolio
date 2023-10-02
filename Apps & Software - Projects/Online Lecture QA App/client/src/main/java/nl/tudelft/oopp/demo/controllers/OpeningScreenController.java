package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.views.CreateRoomDisplay;
import nl.tudelft.oopp.demo.views.JoinLectureDisplay;

/**
 * The type Opening screen controller.
 */
public class OpeningScreenController {

    /**
     * The Join lecture btn.
     */
    @FXML
    Button joinLectureBtn;

    /**
     * The Create lecture btn.
     */
    @FXML
    Button createLectureBtn;

    /**
     * Handle btn join lecture.
     *
     * @throws Exception the exception
     */
    public void handleBtnJoinLecture() throws Exception {
        JoinLectureDisplay joinLectureDisplay = new JoinLectureDisplay();
        Stage window = (Stage) joinLectureBtn.getScene().getWindow();
        joinLectureDisplay.start(window);
        window.show();
    }

    /**
     * Handle btn create lecture.
     *
     * @throws Exception the exception
     */
    public void handleBtnCreateLecture() throws Exception {
        CreateRoomDisplay createRoomDisplay = new CreateRoomDisplay();
        Stage window = (Stage) createLectureBtn.getScene().getWindow();
        createRoomDisplay.start(window);
        window.show();
    }


}
