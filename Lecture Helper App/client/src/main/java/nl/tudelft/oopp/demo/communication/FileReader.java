package nl.tudelft.oopp.demo.communication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The type File reader.
 */
public class FileReader {

    /**
     * Reads from the rateLimit file to return the timer of posting questions.
     *
     * @return the timer
     */
    public static int readFile() {
        int timer = 0;
        try {
            File myObj = new File("client/rateLimit");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                timer = myReader.nextInt();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return timer;
    }
}