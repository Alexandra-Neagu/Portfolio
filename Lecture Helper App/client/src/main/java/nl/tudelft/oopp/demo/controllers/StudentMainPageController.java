package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.BannedIpCommunication;
import nl.tudelft.oopp.demo.communication.EventLog;
import nl.tudelft.oopp.demo.communication.FileReader;
import nl.tudelft.oopp.demo.communication.PollCommunication;
import nl.tudelft.oopp.demo.communication.QuestionCommunication;
import nl.tudelft.oopp.demo.communication.Reloader;
import nl.tudelft.oopp.demo.communication.RoomCommunication;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.communication.UserCommunication;
import nl.tudelft.oopp.demo.data.BannedIp;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Poll;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.views.EndedLectureStudentDisplay;
import nl.tudelft.oopp.demo.views.OpeningScreenDisplay;
import nl.tudelft.oopp.demo.views.StudentPollDisplay;

/**
 * The type Student main page controller.
 */
public class StudentMainPageController implements Initializable {

    /**
     * The Too slow btn.
     */
    @FXML
    Button tooSlowBtn;

    /**
     * The Too fast btn.
     */
    @FXML
    Button tooFastBtn;

    /**
     * The Leave btn.
     */
    @FXML
    Button leaveBtn;
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
     * The Question text box.
     */
    @FXML
    TextArea questionTextBox;
    /**
     * The Post quest btn.
     */
    @FXML
    Button postQuestBtn;
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
     * The Reloader.
     */
    Reloader reloader = new Reloader();

    /**
     * The Timeline.
     */
    static Timeline timeline;
    private boolean openPoll = false;
    private Poll latestPoll;

    /**
     * The Pace.
     */
    String pace = "";

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
            timeline.stop();
            OpeningScreenDisplay openingScreenDisplay = new OpeningScreenDisplay();
            Stage window = (Stage) leaveBtn.getScene().getWindow();
            openingScreenDisplay.start(window);
            window.show();
        }
        RoomCommunication.decreaseStudentCount();
    }

    /**
     * Handle the post question button.
     */
    public void handleBtnPost() {
        String content = questionTextBox.getText();
        if (content == null || content.length() < 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Make sure you fully entered your question.",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            Question question = new Question(content);
            question.setAuthorId(MainApp.getCurrentUser().getId());
            question.setLectureId(MainApp.getCurrentUser().getRoomId());
            question = QuestionCommunication.postQuestion(question);
            questionTextBox.clear();
            initQuestionList();
            disablePostBtn();
            EventLog.logPostQuestion(UserCommunication.getStudent(question.getAuthorId()),
                    question, MainApp.getCurrentRoom());
        }
    }

    /**
     * Disables the post question button for 15 seconds.
     */
    public void disablePostBtn() {
        new Thread() {
            public void run() {
                int readFile = FileReader.readFile();
                Platform.runLater(new Runnable() {
                    public void run() {
                        postQuestBtn.setDisable(true);
                    }
                });
                try {
                    Thread.sleep(readFile);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        postQuestBtn.setDisable(false);
                    }
                });
            }
        }.start();
    }

    /**
     * Init question list.
     */
    public void initQuestionList() {
        checkIfBanned();

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
        Node node;
        for (Question q : questionList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/listItemStudent.fxml"));
                node = (Node) loader.load();
                ListItemStudentController listItemStudentController = loader.getController();
                listItemStudentController.fillQuestion(q);

                questionHolder.getChildren().add(node);
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
        String start = MainApp.getCurrentRoom().getStartTime().toString().substring(0, 5);
        String end = MainApp.getCurrentRoom().getEndTime().toString().substring(0, 5);
        lectureTimeText.setText(start + " - " + end);
        Lecturer lecturer = UserCommunication.getLecturers();
        lecturerName.setText(lecturer.getName());

        //automatic question field reload
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            initQuestionList();
            checkIfRoomClosed();
            try {
                checkIfPollExists();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                checkIfPollIsClosed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.play();
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

    /**
     * Handle too slow button.
     *
     * @param actionEvent the action event
     */
    public void handleTooSlowBtn(ActionEvent actionEvent) {
        RoomCommunication.incrementTooSlowCount();
        disableTooFastSlowBtns();
        pace = "too slow.";
        EventLog.logTooFastSlow(MainApp.getCurrentUser(), pace, MainApp.getCurrentRoom());
    }

    /**
     * Handle too fast button.
     *
     * @param actionEvent the action event
     */
    public void handleTooFastBtn(ActionEvent actionEvent) {
        RoomCommunication.incrementTooFastCount();
        disableTooFastSlowBtns();
        pace = "too fast.";
        EventLog.logTooFastSlow(MainApp.getCurrentUser(), pace, MainApp.getCurrentRoom());
    }

    /**
     * Disables the too fast slow buttons for 10 minutes.
     */
    public void disableTooFastSlowBtns() {
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        tooSlowBtn.setDisable(true);
                        tooFastBtn.setDisable(true);
                    }
                });
                try {
                    Thread.sleep(600000); //10 minutes
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        tooSlowBtn.setDisable(false);
                        tooFastBtn.setDisable(false);
                    }
                });
            }
        }.start();
    }

    /**
     * Check if room closed.
     */
    public void checkIfRoomClosed() {
        long roomId = MainApp.getCurrentRoom().getId();
        MainApp.setCurrentRoom(RoomCommunication.getRoom(roomId));
        if (MainApp.getCurrentRoom().getHasBeenClosed()) {
            try {
                EndedLectureStudentDisplay endedScreen = new EndedLectureStudentDisplay();
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
     * Check if room closed.
     *
     * @throws IOException the io exception
     */
    public void checkIfPollExists() throws IOException {
        Student current = (Student) MainApp.getCurrentUser();
        Poll store = getCurrentPoll();
        if (getCurrentPoll() != null && !current.getAttendedPolls().contains(store.getId())) {

            current.setVotedAnswer(-1);
            StudentPollDisplay studentPollDisplay = new StudentPollDisplay();
            Stage window = (Stage) leaveBtn.getScene().getWindow();
            studentPollDisplay.start(window);
            window.show();
            timeline.stop();
        }
    }

    /**
     * Gets the currently open poll.
     *
     * @return the currently open poll
     */
    public Poll getCurrentPoll() {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        for (int i = 0; i < poll.size(); i++) {
            if (poll.get(i).getOpen()) {
                latestPoll = poll.get(i);
                return poll.get(i);
            }
        }
        return null;
    }

    /**
     * returns the latest poll not regarding wether its open or not.
     *
     * @return the latest poll
     */
    public Poll getLastPoll() {
        long roomId = MainApp.getCurrentRoom().getId();
        List<Poll> poll = PollCommunication.getAllPollsForRoom(roomId);
        if (poll.size() > 0) {
            return poll.get(poll.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Checks whether or no the latest poll is closed
     * and if it is closed it displays the results of
     * the poll if the creator allowed it.
     *
     * @throws IOException if necessary
     */
    public void checkIfPollIsClosed() throws IOException {

        Poll poll = getLastPoll();
        if (poll != null) {
            if (!poll.getOpen()) {
                if (poll.getSharing()) {
                    for (int i = 2; i <= poll.getAnswers().size(); i++) {
                        setVisibility(i, poll);
                    }
                    Student current = (Student) MainApp.getCurrentUser();

                    if (poll.getCorrectAnswerNo() != null) {
                        if (current.getVotedAnswer().equals(poll.getCorrectAnswerNo())
                                || current.getVotedAnswer().equals(-1)) {
                            markCorrectAnswer(poll.getCorrectAnswerNo());
                        } else {
                            markCorrectAnswer(poll.getCorrectAnswerNo());
                            markVotedAnswer(current.getVotedAnswer(), poll.getCorrectAnswerNo());
                        }
                    }
                }
            }
        }
    }

    /**
     * Re start timeline.
     */
    public static void reStartTimeline() {
        timeline.play();
    }

    /**
     * Turns on and off the answers and amount of votes
     * for the latest closed poll if that poll allowed
     * the results to be shared.
     *
     * @param number amount of options
     * @param poll   the latest closed poll
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
                numOfVotes0.setText(String.valueOf(poll.getVotes().get(0)));
                numOfVotes1.setText(String.valueOf(poll.getVotes().get(1)));
                break;
            case 3:
                button2.setText(poll.getAnswers().get(2));
                button2.setVisible(true);
                numOfVotes2.setVisible(true);
                numOfVotes2.setText(String.valueOf(poll.getVotes().get(2)));
                break;
            case 4:
                button3.setText(poll.getAnswers().get(3));
                button3.setVisible(true);
                numOfVotes3.setVisible(true);
                numOfVotes3.setText(String.valueOf(poll.getVotes().get(3)));
                break;
            case 5:
                button4.setText(poll.getAnswers().get(4));
                button4.setVisible(true);
                numOfVotes4.setVisible(true);
                numOfVotes4.setText(String.valueOf(poll.getVotes().get(4)));
                break;
            case 6:
                button5.setText(poll.getAnswers().get(5));
                button5.setVisible(true);
                numOfVotes5.setVisible(true);
                numOfVotes5.setText(String.valueOf(poll.getVotes().get(5)));
                break;
            case 7:
                button6.setText(poll.getAnswers().get(6));
                button6.setVisible(true);
                numOfVotes6.setVisible(true);
                numOfVotes6.setText(String.valueOf(poll.getVotes().get(6)));
                break;
            case 8:
                button7.setText(poll.getAnswers().get(7));
                button7.setVisible(true);
                numOfVotes7.setVisible(true);
                numOfVotes7.setText(String.valueOf(poll.getVotes().get(7)));
                break;
            case 9:
                button8.setText(poll.getAnswers().get(8));
                button8.setVisible(true);
                numOfVotes8.setVisible(true);
                numOfVotes8.setText(String.valueOf(poll.getVotes().get(8)));
                break;
            case 10:
                button9.setText(poll.getAnswers().get(9));
                button9.setVisible(true);
                numOfVotes9.setVisible(true);
                numOfVotes9.setText(String.valueOf(poll.getVotes().get(9)));
                break;
            default:
                break;
        }
    }

    /**
     * Marks the correct answer in the poll stats tab.
     *
     * @param correctAnswer the answer that has been selected as correct
     */
    public void markCorrectAnswer(int correctAnswer) {
        switch (correctAnswer) {
            case 0:
                button0.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 1:
                button1.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 2:
                button2.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 3:
                button3.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 4:
                button4.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 5:
                button5.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 6:
                button6.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 7:
                button7.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 8:
                button8.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 9:
                button9.setStyle("-fx-background-color: #7FFF00;\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            default:
                break;
        }
    }

    /**
     * Colors in the answer the user filled in in case
     * he or she filled in the incorrect answer.
     *
     * @param votedAnswer answer user filled in
     * @param correctAns  the correct ans
     */
    public void markVotedAnswer(int votedAnswer, int correctAns) {
        String color = correctAns == -1 ? "#15a8d1" : "#980101";


        switch (votedAnswer) {
            case 0:
                button0.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 1:
                button1.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 2:
                button2.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 3:
                button3.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 4:
                button4.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 5:
                button5.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 6:
                button6.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 7:
                button7.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 8:
                button8.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            case 9:
                button9.setStyle("-fx-background-color: " + color + ";\n"
                        + "-fx-text-fill: black;\n"
                        + "-fx-end-margin: black");
                break;
            default:
                break;
        }
    }

    /**
     * If the user is banned, close the lecture page and open the banned page.
     */
    public void checkIfBanned() {
        /*for (BannedIp ip : ServerCommunication.getAllBannedIps()) {
            if (ip.getIpAddress().equals(MainApp.getCurrentUser().getIpAddress())) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    URL xmlUrl = getClass().getResource("/StudentBannedPage.fxml");
                    loader.setLocation(xmlUrl);
                    Parent root = loader.load();

                    Stage window = (Stage) leaveBtn.getScene().getWindow();
                    window.setScene(new Scene(root));
                    //Insert timeline.stop() here.
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Check your connection.", ButtonType.OK);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }
        }**/
        for (Long roomId : BannedIpCommunication
                .getBannedRooms(MainApp.getCurrentUser().getIpAddress())) {
            if (roomId.equals(MainApp.getCurrentUser().getRoomId())) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    URL xmlUrl = getClass().getResource("/StudentBannedPage.fxml");
                    loader.setLocation(xmlUrl);
                    Parent root = loader.load();

                    Stage window = (Stage) leaveBtn.getScene().getWindow();
                    window.setScene(new Scene(root));
                    timeline.stop();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Check your connection.", ButtonType.OK);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }
        }
    }

}
