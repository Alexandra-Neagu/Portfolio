package nl.tudelft.oopp.demo.controllers;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.MainApp;

/**
 * The type Ended lecture student controller.
 */
public class EndedLectureStudentController {

    /**
     * The Back to menu btn.
     */
    @FXML
    Button backToMenuBtn;

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

        MainApp.setCurrentRoom(null);
        MainApp.setCurrentUser(null);
    }
}
