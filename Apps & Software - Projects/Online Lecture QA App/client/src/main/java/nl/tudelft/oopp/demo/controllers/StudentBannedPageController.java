package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.views.OpeningScreenDisplay;


/**
 * The type Student banned page controller.
 */
public class StudentBannedPageController {
    /**
     * The Back to menu btn.
     */
    @FXML
    Button backToMenuBtn;

    /**
     * The Quit app btn.
     */
    @FXML
    Button quitAppBtn;

    /**
     * Opens the main menu.
     * throws IOException
     *
     * @throws IOException the io exception
     */
    public void handleBackToMenu() throws IOException {
        OpeningScreenDisplay openingScreenDisplay = new OpeningScreenDisplay();
        Stage window = (Stage) backToMenuBtn.getScene().getWindow();
        openingScreenDisplay.start(window);
        window.show();
    }

    /**
     * Quits the application.
     */
    public void handleQuitAppBtn() {
        Stage stage = (Stage) quitAppBtn.getScene().getWindow();
        stage.close();
    }

}
