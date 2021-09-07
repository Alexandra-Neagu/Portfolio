package nl.tudelft.oopp.demo.controllers;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.RoomCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

/**
 * The type Ended lecture mod lect controller.
 */
public class EndedLectureModLectController {


    /**
     * The Download file btn.
     */
    @FXML
    Button downloadFileBtn;

    /**
     * The Back to menu btn.
     */
    @FXML
    Button backToMenuBtn;

    /**
     * The Download label.
     */
    @FXML
    Label downloadLabel;

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

        RoomCommunication.reopenRoomForModLect();

        MainApp.setCurrentRoom(null);
        MainApp.setCurrentUser(null);
    }

    /**
     * Handle download btn.
     */
    public void handleDownloadBtn() {
        String message = ServerCommunication.createExportFile(MainApp.getCurrentRoom()
                                                                .getLecturerRoomId());
        downloadLabel.setText(message);
        downloadLabel.setDisable(false);
        if (message.contains("successfully")) {
            downloadFileBtn.setDisable(true);
        }
    }
}
