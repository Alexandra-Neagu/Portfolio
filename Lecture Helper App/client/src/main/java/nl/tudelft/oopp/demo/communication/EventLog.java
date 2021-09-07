package nl.tudelft.oopp.demo.communication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import nl.tudelft.oopp.demo.data.Answer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Poll;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.data.User;


/**
 * The type Event log.
 */
public class EventLog {
    /**
     * The File writer.
     */
    static FileWriter fileWriter;
    /**
     * The File.
     */
    static File file;


    /**
     * Create an event log file for the current room.
     *
     * @param room current room
     */
    public static void createEventLog(Room room) {
        String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
        String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
        file = new File(desktopPath + "\\" + fileName);

        boolean result;

        try {
            result = file.createNewFile();
            if (result) {
                System.out.println("Created");
            } else {
                System.out.println("File already exist at location: " + file.getCanonicalPath());
            }
            try {
                fileWriter = new FileWriter(file);
                System.out.println("FileWriter has a File");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();    //prints exception if any
        }
    }

    /**
     * Write the room creation into event log file.
     *
     * @param room current room
     */
    public static void logRoomCreated(Room room) {
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write("Room created: ");
            fileWriter.write("\n");
            fileWriter.write(room.toString());
            fileWriter.write("\n\n\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write the users who joined into the event log.
     *
     * @param user the user
     * @param room current room
     */
    public static void logJoins(User user, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + user.getTimeOfJoin().toString().substring(11,16)
                        + "] " + user.getName() + " joined.");
                fileWriter.write("\n");
                fileWriter.write(user.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the posted questions into the event log.
     *
     * @param user     the student
     * @param question the question
     * @param room     current room
     */
    public static void logPostQuestion(User user, Question question, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + question.getTimePublished()
                        .toString().substring(11,16) + "] "
                        + "Student " + user.getName() + " posted a question: ");
                fileWriter.write("\n");
                fileWriter.write("IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write(question.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the deleted question into the event log.
     *
     * @param user     the user
     * @param question the question
     * @param room     current room
     */
    public static void logDeleteQuestion(User user, Question question, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now()
                        .toString().substring(11,16) + "] "
                        + user.getClass().getSimpleName() + " "
                        + user.getName() + " deleted a question: ");
                fileWriter.write("\n");
                fileWriter.write("IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write(question.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the rephrased question into the event log.
     *
     * @param user     the user
     * @param question the question
     * @param room     current room
     */
    public static void logRephraseQuestion(User user, Question question, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getClass().getSimpleName() + " "
                        + user.getName() + " rephrased a question: ");
                fileWriter.write("\n");
                fileWriter.write("IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write(question.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write the banned student into event log.
     *
     * @param user       the user
     * @param bannedUser the banned user
     * @param room       current room
     */
    public static void logBanStudent(User user, User bannedUser, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getClass().getSimpleName() + " "
                        + user.getName() + " banned a student: ");
                fileWriter.write("\n");
                fileWriter.write(user.getClass().getSimpleName() + " IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write("Banned Student: " + bannedUser.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write the users who left the room into the event log.
     *
     * @param user the user
     * @param room current room
     */
    public static void logLeft(User user, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getName() + " left.");
                fileWriter.write("\n");
                fileWriter.write(user.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write answers in the event log.
     *
     * @param user     the user
     * @param question the question
     * @param answer   the answer
     * @param room     current room
     */
    public static void logAnswer(User user, Question question, Answer answer, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + answer.getTimePublished()
                        .toString().substring(11,16) + "] "
                        + user.getClass().getSimpleName() + " "
                        + user.getName() + " answered a question: ");
                fileWriter.write("\n");
                fileWriter.write("IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write(question.toString());
                fileWriter.write("\n");
                fileWriter.write("Answer: " + answer.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the marking as answered event into the event log.
     *
     * @param user     the user
     * @param question the question
     * @param room     current room
     */
    public static void logMarkAsAnswered(User user, Question question, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getClass().getSimpleName() + " "
                        + user.getName() + " marked a question as answered: ");
                fileWriter.write("\n");
                fileWriter.write("IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write(question.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the upvote event in the event log.
     *
     * @param user     the user
     * @param question the question
     * @param room     current room
     */
    public static void logUpvoteQuestion(User user, Question question, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getClass().getSimpleName() + " "
                        + user.getName() + " upvoted a question: ");
                fileWriter.write("\n");
                fileWriter.write("IP: " + user.getIpAddress());
                fileWriter.write("\n");
                fileWriter.write(question.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write too fast/slow events in the event log.
     *
     * @param user the user
     * @param pace the pace (fast/slow)
     * @param room current room
     */
    public static void logTooFastSlow(User user, String pace, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);
            
            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getName() + " thinks the lecturer is " + pace);
                fileWriter.write("\n");
                fileWriter.write(user.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write when the lecturer resets the pace votes into the event log.
     *
     * @param user the User
     * @param room the curren room
     */
    public static void logResetPace(User user, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getName() + " reset the lecture pace votes. ");
                fileWriter.write("\n");
                fileWriter.write(user.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write when a poll is created.
     *
     * @param user the user
     * @param poll the poll
     * @param room current room
     */
    public static void logCreatePoll(User user, Poll poll, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getName() + " created a poll: ");
                fileWriter.write("\n");
                fileWriter.write(user.toString());
                fileWriter.write("\n");
                fileWriter.write(poll.toString());
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write when a student votes for an answer on a poll.
     *
     * @param user        the user
     * @param poll        the poll
     * @param votedAnswer the voted answer
     * @param room        current room
     */
    public static void logVotePoll(User user, Poll poll, Integer votedAnswer, Room room) {
        try {
            String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
            String fileName = "EventLog" + "_room(" + room.getId() + ")" + ".txt";
            File file = new File(desktopPath + "\\" + fileName);

            if (file.isFile()) {
                fileWriter = new FileWriter(file, true);
                fileWriter.write("[" + LocalDateTime.now().toString().substring(11,16) + "] "
                        + user.getName() + " answered a poll: ");
                fileWriter.write("\n");
                fileWriter.write(user.toString());
                fileWriter.write("\n");
                fileWriter.write(poll.toString());
                fileWriter.write("\n");
                fileWriter.write("The student voted for the answer with id: " + votedAnswer);
                fileWriter.write("\n\n");
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

