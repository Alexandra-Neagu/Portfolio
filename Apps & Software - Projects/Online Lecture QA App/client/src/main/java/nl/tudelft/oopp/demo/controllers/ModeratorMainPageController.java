package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.PollCommunication;
import nl.tudelft.oopp.demo.communication.QuestionCommunication;
import nl.tudelft.oopp.demo.communication.Reloader;
import nl.tudelft.oopp.demo.communication.RoomCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Poll;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.views.CreatePollDisplay;
import nl.tudelft.oopp.demo.views.EndedLectureModLectDisplay;
import nl.tudelft.oopp.demo.views.OpeningScreenDisplay;

/**
 * The type Moderator main page controller.
 */
public class ModeratorMainPageController implements Initializable {

    /**
     * The Lecture info.
     */
    @FXML
    Label lectureInfo;
    /**
     * The Lecturer name.
     */
    @FXML
    Label lecturerName;
    /**
     * The Lecture time text.
     */
    @FXML
    Label lectureTimeText;

    /**
     * The Question holder.
     */
    @FXML
    VBox questionHolder;
    /**
     * The Unansweredq holder.
     */
    @FXML
    VBox unansweredqHolder;

    /**
     * The Answeredq holder.
     */
    @FXML
    VBox answeredqHolder;

    /**
     * The Leave btn.
     */
    @FXML
    Button leaveBtn;

    /**
     * The Create poll button.
     */
    @FXML
    Button createPollBtn;

    /**
     * The close poll button.
     */
    @FXML
    Button closePoll;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button0;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button1;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button2;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button3;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button4;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button5;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button6;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button7;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button8;

    /**
     * The Create Poll button.
     */
    @FXML
    Button button9;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label pollAlert;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes0;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes1;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes2;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes3;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes4;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes5;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes6;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes7;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes8;

    /**
     * The Load poll options Label.
     */
    @FXML
    Label numOfVotes9;

    /**
     * The Timeline.
     */
    static Timeline timeline;

    /**
     * The Sort by.
     */
    @FXML
    MenuButton sortBy;

    /**
     * The Sort by 2.
     */
    @FXML
    MenuButton sortBy2;

    /**
     * The Sort by 3.
     */
    @FXML
    MenuButton sortBy3;

    /**
     * The Reloader.
     */
    Reloader reloader = new Reloader();


    /**
     * Leave button.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void leaveButton(ActionEvent actionEvent) throws IOException {

        //Creates new alert to ask the user if they are sure.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        //If the user presses the 'yes' button, app will take the user to the main menu.
        if (alert.getResult() == ButtonType.YES) {
            EventLog.logLeft(MainApp.getCurrentUser(), MainApp.getCurrentRoom());
            OpeningScreenDisplay openingScreenDisplay = new OpeningScreenDisplay();
            Stage window = (Stage) leaveBtn.getScene().getWindow();
            openingScreenDisplay.start(window);
            window.show();

        }
    }

    /**
     * Init question list.
     */
    public void initQuestionList() {

        String ageSorterId = reloader.getSortId();
        String ageSorterText = reloader.getSortText();
        String ageSorterIdAns = reloader.getSortIdAns();
        String ageSorterTextAns = reloader.getSortTextAns();
        String ageSorterIdUnans = reloader.getSortIdUnans();
        String ageSorterTextUnans = reloader.getSortTextUnans();

        sortBySwitch(ageSorterId, ageSorterText);
        sortBySwitch(ageSorterIdAns, ageSorterTextAns);
        sortBySwitch(ageSorterIdUnans, ageSorterTextUnans);
    }

    /**
     * Load questions.
     *
     * @param questionHolder the question holder
     * @param questionList   the question list
     */
    public void loadQuestions(VBox questionHolder, List<Question> questionList) {
        Node[] nodes = new Node[questionList.size()];
        int i = 0;
        for (Question q : questionList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/listItemModerator.fxml"));
                nodes[i] = (Node) loader.load();
                ListItemModeratorController listItemModeratorController = loader.getController();
                listItemModeratorController.fillQuestion(q);
                questionHolder.getChildren().add(nodes[i++]);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Couldn't load any questions. Check your connection.", ButtonType.OK);
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initQuestionList();
        lectureInfo.setText(MainApp.getCurrentRoom().getCourseName() + ", "
                + MainApp.getCurrentRoom().getLectureName());
        String start = MainApp.getCurrentRoom().getStartTime().toString().substring(0,5);
        String end = MainApp.getCurrentRoom().getEndTime().toString().substring(0,5);
        lectureTimeText.setText(start + " - " + end);
        Lecturer lecturer = UserCommunication.getLecturers();
        lecturerName.setText(lecturer.getName());

        //automatic question field reload
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            initQuestionList();
            checkIfRoomClosed();
            long roomId = MainApp.getCurrentRoom().getId();
            List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
            for (int i = 0; i < poll.size(); i++) {
                if (poll.get(i).getOpen()) {
                    handlePollVotes();
                }
            }
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.play();
    }

    /**
     * Check if room closed.
     */
    public void checkIfRoomClosed() {
        long roomId = MainApp.getCurrentRoom().getId();
        MainApp.setCurrentRoom(RoomCommunication.getRoom(roomId));
        if (MainApp.getCurrentRoom().getHasBeenClosed() == true) {
            try {
                EndedLectureModLectDisplay endedScreen = new EndedLectureModLectDisplay();
                Stage window = (Stage) leaveBtn.getScene().getWindow();
                endedScreen.start(window);
                window.show();
                timeline.stop();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * When button is pressed this method makes sure that the
     * scene for creating pols is opened.
     *
     * @throws Exception if necessary
     */
    public void openCreatePollMenu() throws Exception {
        CreatePollDisplay createPollDisplay = new CreatePollDisplay();
        Stage window = (Stage) createPollBtn.getScene().getWindow();
        createPollDisplay.start(window);
        window.show();
        closePoll.setVisible(false);
        closeCurrentPoll();
        timeline.stop();
    }

    /**
     * Closes the current running poll(s).
     *
     * @throws IOException if necessary
     */
    public void closeCurrentPoll() throws IOException {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        for (int i = 0; i < poll.size(); i++) {
            if (poll.get(i).getOpen()) {
                PollCommunication.closePoll(poll.get(i).getId());
                pollAlert.setVisible(true);
                closePoll.setVisible(false);
            }
        }
    }

    /**
     * Checks and updates the amount of votes for each answer
     * in the current poll.
     */
    public void handlePollVotes() {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        for (int i = 0; i < poll.size(); i++) {
            if (poll.get(i).getOpen()) {
                Poll store = poll.get(i);
                if (store.getVotes().size() >= 2) {
                    numOfVotes0.setText(String.valueOf(store.getVotes().get(0)));
                    numOfVotes1.setText(String.valueOf(store.getVotes().get(1)));

                    if (store.getVotes().size() >= 3) {
                        numOfVotes2.setText(String.valueOf(store.getVotes().get(2)));
                    }
                    if (store.getVotes().size() >= 4) {
                        numOfVotes3.setText(String.valueOf(store.getVotes().get(3)));
                    }
                    if (store.getVotes().size() >= 5) {
                        numOfVotes4.setText(String.valueOf(store.getVotes().get(4)));
                    }
                    if (store.getVotes().size() >= 6) {
                        numOfVotes5.setText(String.valueOf(store.getVotes().get(5)));
                    }
                    if (store.getVotes().size() >= 7) {
                        numOfVotes6.setText(String.valueOf(store.getVotes().get(6)));
                    }
                    if (store.getVotes().size() >= 8) {
                        numOfVotes7.setText(String.valueOf(store.getVotes().get(7)));
                    }
                    if (store.getVotes().size() >= 9) {
                        numOfVotes8.setText(String.valueOf(store.getVotes().get(8)));
                    }
                    if (store.getVotes().size() == 10) {
                        numOfVotes9.setText(String.valueOf(store.getVotes().get(9)));
                    }
                }
            }
        }
    }

    /**
     * Makes sure the buttons for the stats of the poll are correct
     * and visible.
     */
    public void rewriteTheButtons() {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        for (int i = 0; i < poll.size(); i++) {
            if (poll.get(i).getOpen()) {
                Poll store = poll.get(i);
                pollAlert.setVisible(false);

                for (int j = 2; j <= store.getAnswers().size(); j++) {
                    setVisibility(j, store);
                }
            }

        }
    }

    /**
     * Makes sure the buttons for the stats of the poll are correct
     * and visible.
     *
     * @param number the number
     * @param poll   the poll
     */
    public void setVisibility(int number, Poll poll) {
        switch (number) {
            case 2:
                button0.setText(poll.getAnswers().get(0));
                button1.setText(poll.getAnswers().get(1));
                button0.setVisible(true);
                button1.setVisible(true);
                numOfVotes0.setVisible(true);
                numOfVotes1.setVisible(true);
                break;
            case 3:
                button2.setText(poll.getAnswers().get(2));
                button2.setVisible(true);
                numOfVotes2.setVisible(true);
                break;
            case 4:
                button3.setText(poll.getAnswers().get(3));
                button3.setVisible(true);
                numOfVotes3.setVisible(true);
                break;
            case 5:
                button4.setText(poll.getAnswers().get(4));
                button4.setVisible(true);
                numOfVotes4.setVisible(true);
                break;
            case 6:
                button5.setText(poll.getAnswers().get(5));
                button5.setVisible(true);
                numOfVotes5.setVisible(true);
                break;
            case 7:
                button6.setText(poll.getAnswers().get(6));
                button6.setVisible(true);
                numOfVotes6.setVisible(true);
                break;
            case 8:
                button7.setText(poll.getAnswers().get(7));
                button7.setVisible(true);
                numOfVotes7.setVisible(true);
                break;
            case 9:
                button8.setText(poll.getAnswers().get(8));
                button8.setVisible(true);
                numOfVotes8.setVisible(true);
                break;
            case 10:
                button9.setText(poll.getAnswers().get(9));
                button9.setVisible(true);
                numOfVotes9.setVisible(true);
                break;
            default:
                break;
        }
    }

    /**
     * Inputs the action event which is used to call the sortBySwitch.
     *
     * @param event the event
     */
    public void handleSortByButton(ActionEvent event) {
        MenuItem currentItem = (MenuItem) event.getSource();

        String id = currentItem.getId();
        String text = currentItem.getText();

        sortBySwitch(id, text);

    }

    /**
     * Returns the list of questions based on which menu item is used.
     *
     * @param id   the id of the menu item
     * @param text the text of the menu item
     */
    public void sortBySwitch(String id, String text) {
        final String Sort_pop = "Most Relevant";
        final String Sort_age = "Most Recent";
        final String Sort_upvote = "Most upvotes";
        switch (id + " " + text) {
            case "1 1":
                questionHolder.getChildren().clear();
                List<Question> allQuestionsList = QuestionCommunication.getQuestions();
                if (allQuestionsList != null) {
                    loadQuestions(questionHolder, allQuestionsList);
                }
                break;
            case "2 2":
                unansweredqHolder.getChildren().clear();
                List<Question> unansQuestionsList = QuestionCommunication.getUnansQuestions();
                if (unansQuestionsList != null) {
                    loadQuestions(unansweredqHolder, unansQuestionsList);
                }
                break;
            case "3 3":
                answeredqHolder.getChildren().clear();
                List<Question> ansQuestionsList = QuestionCommunication.getAnsQuestions();
                if (ansQuestionsList != null) {
                    loadQuestions(answeredqHolder, ansQuestionsList);
                }
                break;
            case "sortBy Most Relevant":
                questionHolder.getChildren().clear();
                List<Question> popularityList = QuestionCommunication.getQuestionsSortedByPop();
                if (popularityList != null) {
                    loadQuestions(questionHolder, popularityList);
                }
                changeSortByName(id, Sort_pop);
                reloader.setSortId(id);
                reloader.setSortText(text);
                break;
            case "sortBy2 Most Relevant":
                unansweredqHolder.getChildren().clear();
                List<Question> popularityList2 = QuestionCommunication.getQsbpUnanswered();
                if (popularityList2 != null) {
                    loadQuestions(unansweredqHolder, popularityList2);
                }
                changeSortByName(id, Sort_pop);
                reloader.setSortIdUnans(id);
                reloader.setSortTextUnans(text);
                break;
            case "sortBy3 Most Relevant":
                answeredqHolder.getChildren().clear();
                List<Question> popularityList3 = QuestionCommunication.getQsbpAnswered();
                if (popularityList3 != null) {
                    loadQuestions(answeredqHolder, popularityList3);
                }
                changeSortByName(id, Sort_pop);
                reloader.setSortIdAns(id);
                reloader.setSortTextAns(text);
                break;
            case "sortBy Most Recent":
                questionHolder.getChildren().clear();
                List<Question> ageList = QuestionCommunication.getQuestionsSortedByAge();
                if (ageList != null) {
                    loadQuestions(questionHolder, ageList);
                }
                changeSortByName(id, Sort_age);
                reloader.setSortId(id);
                reloader.setSortText(text);
                break;
            case "sortBy2 Most Recent":
                unansweredqHolder.getChildren().clear();
                List<Question> ageList2 = QuestionCommunication.getQsbaUnanswered();
                if (ageList2 != null) {
                    loadQuestions(unansweredqHolder, ageList2);
                }
                changeSortByName(id, Sort_age);
                reloader.setSortIdUnans(id);
                reloader.setSortTextUnans(text);
                break;
            case "sortBy3 Most Recent":
                answeredqHolder.getChildren().clear();
                List<Question> ageList3 = QuestionCommunication.getQsbaAnswered();
                if (ageList3 != null) {
                    loadQuestions(answeredqHolder, ageList3);
                }
                changeSortByName(id, Sort_age);
                reloader.setSortIdAns(id);
                reloader.setSortTextAns(text);
                break;
            case "sortBy Most upvotes":
                questionHolder.getChildren().clear();
                List<Question> upvotesList = QuestionCommunication.getQuestionsSortedByUpvotes();
                if (upvotesList != null) {
                    loadQuestions(questionHolder, upvotesList);
                }
                changeSortByName(id, Sort_upvote);
                reloader.setSortId(id);
                reloader.setSortText(text);
                break;
            case "sortBy2 Most upvotes":
                unansweredqHolder.getChildren().clear();
                List<Question> upvotesList2 = QuestionCommunication.getQsbuUnanswered();
                if (upvotesList2 != null) {
                    loadQuestions(unansweredqHolder, upvotesList2);
                }
                changeSortByName(id, Sort_upvote);
                reloader.setSortIdUnans(id);
                reloader.setSortTextUnans(text);
                break;
            case "sortBy3 Most upvotes":
                answeredqHolder.getChildren().clear();
                List<Question> upvotesList3 = QuestionCommunication.getQsbuAnswered();
                if (upvotesList3 != null) {
                    loadQuestions(answeredqHolder, upvotesList3);
                }
                changeSortByName(id, Sort_upvote);
                reloader.setSortIdAns(id);
                reloader.setSortTextAns(text);
                break;
            default:
                System.out.println("Could not get Id or Text.");
        }
    }

    /**
     * Changes the sort by button to the one you sorted to.
     *
     * @param id           the id of the button
     * @param sortByString the text of the button
     */
    public void changeSortByName(String id, String sortByString) {
        final String Sort_1 = "sortBy";
        final String Sort_2 = "sortBy2";
        final String Sort_3 = "sortBy3";
        switch (id) {
            case Sort_1:
                sortBy.setText(sortByString);
                break;
            case Sort_2:
                sortBy2.setText(sortByString);
                break;
            case Sort_3:
                sortBy3.setText(sortByString);
                break;
            default:
                System.out.println("Could not get Id or sortByString.");
        }
    }
}
