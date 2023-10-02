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
import java.util.List;

import nl.tudelft.oopp.demo.data.BannedIp;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;


/**
 * The type Banned ip communication.
 */
public class BannedIpCommunication {
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
     * Ban IP.
     *
     * @param bannedIp the banned ip
     * @return banned ip
     */
    public static BannedIp banIP(BannedIp bannedIp) {
        String requestBody = gson.toJson(bannedIp);
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/bannedIps/insert")).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(),  BannedIp.class);
    }

    /**
     * Gets all banned ips.
     *
     * @return the all banned ips
     */
    public static List<BannedIp> getAllBannedIps() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/bannedIps")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<List<BannedIp>>() {
        }.getType());
    }

    /**
     * Gets the banned rooms.
     *
     * @param ip the ip
     * @return List of ids of the banned rooms
     */
    public static List<Long> getBannedRooms(String ip) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/bannedIps/roomsBanned/" + ip)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Long>>() {
        }.getType());
    }
}
