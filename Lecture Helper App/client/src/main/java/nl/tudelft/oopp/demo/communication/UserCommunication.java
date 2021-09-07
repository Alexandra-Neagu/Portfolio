package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;

/**
 * The type User communication.
 */
public class UserCommunication {
    private static HttpClient client = HttpClient.newBuilder().build();
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();


    /**
     * Gets the lecturer who created the room.
     *
     * @return the lecturer
     */
    public static Lecturer getLecturers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/lecturers/findFirstLecturer/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<Lecturer>() {
        }.getType());
    }

    /**
     * Gets the Moderator Object.
     *
     * @param modId the moderator id
     * @return the moderator
     */
    public static Moderator getModerator(long modId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/moderators/get/" + modId)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Moderator.class);
    }

    /**
     * Create user user.
     *
     * @param user the user
     * @return the user
     */
    public static User createUser(User user) {
        String requestBody = gson.toJson(user);
        String uri = "http://localhost:8080/";
        switch (user.getClass().getSimpleName()) {
            case "Student":
                uri += "students/insert";
                break;
            case "Moderator":
                uri += "moderators/insert";
                break;
            case "Lecturer":
                uri += "lecturers/insert";
                break;
            default:
        }

        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(uri)).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            System.out.println("ResponseBody" + response.body());
        }
        return gson.fromJson(response.body(), user.getClass());
    }

    /**
     * Join lecture user.
     *
     * @param roomCode the room code
     * @param user     the user
     * @return the user
     */
    public static User joinLecture(String roomCode, User user) {
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .GET()
                .uri(URI.create("http://localhost:8080/rooms/join?code=" + roomCode + "&name=" + user.getName().replace(" ", "%20"))).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        System.out.println(response.body());
        return gson.fromJson(response.body(), user.getClass());
    }

    /**
     * Delete student.
     *
     * @param studentId the student id
     */
    public static void deleteStudent(long studentId) {
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/students/delete/" + studentId)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Gets room.
     *
     * @param roomId the room id
     * @return the room
     */
    public static Room getRoom(long roomId) {
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .GET()
                .uri(URI.create("http://localhost:8080/rooms/get/" + roomId)).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Room.class);
    }


    /**
     * Gets the Lecturer Object.
     *
     * @param lecturerId the lecturer id
     * @return the lecturer
     */
    public static Lecturer getLecturer(long lecturerId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/lecturers/get/" + lecturerId)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Lecturer.class);
    }

    /**
     * Get the Student.
     *
     * @param studentId the student id
     * @return student student
     */
    public static Student getStudent(long studentId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/students/get/" + studentId)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Student.class);
    }
}
