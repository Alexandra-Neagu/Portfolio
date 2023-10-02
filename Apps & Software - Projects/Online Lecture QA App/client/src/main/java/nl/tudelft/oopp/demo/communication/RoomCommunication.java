package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;

/**
 * The type Room communication.
 */
public class RoomCommunication {
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
     * Create room room.
     *
     * @param room the room
     * @return the room
     */
    public static Room createRoom(Room room) {
        String requestBody = gson.toJson(room);
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/rooms/insert")).build();

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
        return gson.fromJson(response.body(), Room.class);
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
     * Increment too fast count integer.
     *
     * @return the integer
     */
    public static Integer incrementTooFastCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/incrementTooFast/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Increment too slow count integer.
     *
     * @return the integer
     */
    public static Integer incrementTooSlowCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/incrementTooSlow/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Gets too fast count integer.
     *
     * @return the integer
     */
    public static Integer getTooFastCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/getTooFast/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Sets the too fast count integer.
     *
     * @return the integer
     */
    public static Integer setTooFastCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/setTooFast/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Gets too slow count integer.
     *
     * @return the integer
     */
    public static Integer getTooSlowCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/getTooSlow/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Sets the too slow count integer.
     *
     * @return the integer
     */
    public static Integer setTooSlowCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/setTooSlow/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Increases the student count integer.
     *
     * @return the integer
     */
    public static Integer increaseStudentCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/increaseStudentCount/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Decreases the student count integer.
     *
     * @return the integer
     */
    public static Integer decreaseStudentCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/decreaseStudentCount/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Gets the student count integer.
     *
     * @return the integer
     */
    public static Integer getStudentCount() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/getStudentCount/" + MainApp.getCurrentUser().getRoomId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Integer.class);
    }

    /**
     * Close room room.
     *
     * @return the room
     */
    public static Room closeRoom() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/close/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), Room.class);
    }

    /**
     * Reopen room for mod lect room.
     *
     * @return the room
     */
    public static Room reopenRoomForModLect() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/rooms/reopenForModLect/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), Room.class);
    }
}
