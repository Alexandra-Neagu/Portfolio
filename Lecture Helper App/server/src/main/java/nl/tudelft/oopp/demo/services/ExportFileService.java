package nl.tudelft.oopp.demo.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Export file service.
 */
@Service
public class ExportFileService {

    private final QuestionService questionService;
    private final RoomRepository roomRepository;

    /**
     * Instantiates a new Export file service.
     *
     * @param questionService the question service
     * @param roomRepository  the room repository
     */
    @Autowired
    public ExportFileService(QuestionService questionService, RoomRepository roomRepository) {
        this.questionService = questionService;
        this.roomRepository = roomRepository;
    }

    /**
     * Create file.
     *
     * @param lecturerRoomId the lecturer room code
     * @return the string
     */
    public String createFile(String lecturerRoomId) {

        if (System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
            System.err.println("Sorry, Windows only!");
            return "Sorry, Windows only";
        }

        Room room = roomRepository.findRoomByLecturerRoomId(lecturerRoomId).get(0);
        String desktopPath = (new File(System.getProperty("user.home"), "Desktop")).getPath();
        String fileName = room.getLectureName() + "_qns" + ".txt";
        File exportFile = new File(desktopPath + "\\" + fileName);
        boolean result;

        try {
            result = exportFile.createNewFile();  // creates a new file
            if (result) {     // test if successfully created a new file
                // prints the path string
                System.out.println("File created at location: " + exportFile.getCanonicalPath());
                return "File with name "
                        + fileName
                        + " created on desktop and "
                        + writeFile(exportFile, room);
            } else {
                System.out.println("File already exists at location: "
                        + exportFile.getCanonicalPath());
                return "A file with name " + fileName + " already exists on desktop.";
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();    // prints exception if any
            return "An error occurred.";
        }
    }

    /**
     * Write file.
     *
     * @param exportFile the export file
     * @param room the room
     */
    private String writeFile(File exportFile, Room room) {
        List<Question> questions = questionService.findAllByRoomId(room.getId());
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(exportFile);
            fileWriter.write(room.toStringForFile());
            fileWriter.write("\n\n\n");
            for (Question question : questions) {
                fileWriter.write(question.toStringForFile());
                fileWriter.write("\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
            return "successfully written.";
        } catch (IOException ioException) {
            System.out.println("An error occurred.");
            ioException.printStackTrace();
            return "an error occurred when writing.";
        }
    }
}
