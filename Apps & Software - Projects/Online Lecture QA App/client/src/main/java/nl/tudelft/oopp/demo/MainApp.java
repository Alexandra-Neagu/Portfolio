package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.views.OpeningScreenDisplay;

/**
 * The type Main app.
 */
public class MainApp {

    private static Room currentRoom;
    private static User currentUser;

    /**
     * Gets current room.
     *
     * @return the current room
     */
    public static Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Sets current room.
     *
     * @param currentRoom the current room
     */
    public static void setCurrentRoom(Room currentRoom) {
        MainApp.currentRoom = currentRoom;
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets current user.
     *
     * @param currentUser the current user
     */
    public static void setCurrentUser(User currentUser) {
        MainApp.currentUser = currentUser;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        OpeningScreenDisplay.main(new String[0]);
    }
}
