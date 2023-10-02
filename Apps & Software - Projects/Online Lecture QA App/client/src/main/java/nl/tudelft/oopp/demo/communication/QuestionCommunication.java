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
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;


/**
 * The type Question communication.
 */
public class QuestionCommunication {
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
     * Gets question.
     *
     * @param questionId the question id
     * @return the question
     */
    public static Question getQuestion(long questionId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/get/" + questionId)).build();
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
        return gson.fromJson(response.body(), Question.class);
    }

    /**
     * Mark as answered.
     *
     * @param questionId the question id
     */
    public static void markAsAnswered(long questionId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/markasanswered/" + questionId)).build();
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
     * Upvote question integer.
     *
     * @param questionId the question id
     * @return the integer
     */
    public static Integer upvoteQuestion(long questionId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/upvote/" + questionId)).build();
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
     * Gets questions.
     *
     * @return the questions
     */
    public static List<Question> getQuestions() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/getAllinRoom/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets ans questions.
     *
     * @return the ans questions
     */
    public static List<Question> getAnsQuestions() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/getAllAnswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets unans questions.
     *
     * @return the unans questions
     */
    public static List<Question> getUnansQuestions() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/getAllUnanswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets questions sorted by age.
     *
     * @return the questions sorted by age
     */
    public static List<Question> getQuestionsSortedByAge() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortbyage/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**
     * Gets unanswered questions sorted by age.
     *
     * @return the unanswered questions sorted by age
     */
    public static List<Question> getQsbaUnanswered() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortbyageunanswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets answered questions sorted by age.
     *
     * @return the answered questions sorted by age
     */
    public static List<Question> getQsbaAnswered() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortbyageanswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets questions sorted by upvotes.
     *
     * @return the questions sorted by upvotes
     */
    public static List<Question> getQuestionsSortedByUpvotes() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortByUpvotes/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets unanswered questions sorted by upvotes.
     *
     * @return the unanswered questions sorted by upvotes
     */
    public static List<Question> getQsbuUnanswered() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortByUpvotesUnanswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets answered questions sorted by upvotes.
     *
     * @return the answered questions sorted by upvotes
     */
    public static List<Question> getQsbuAnswered() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortByUpvotesAnswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**
     * Gets the list of questions sorted by popularity.
     *
     * @return the list
     */
    public static List<Question> getQuestionsSortedByPop() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortByPopularity/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Gets the list of unanswered questions sorted by popularity.
     *
     * @return the list
     */
    public static List<Question> getQsbpAnswered() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortByPopularityAnswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**
     * Gets the list of answered questions sorted by popularity.
     *
     * @return the list
     */
    public static List<Question> getQsbpUnanswered() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/questions/sortByPopularityUnanswered/" + MainApp.getCurrentUser().getRoomId())).build();
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
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Delete question.
     *
     * @param questionId the question id
     */
    public static void deleteQuestion(long questionId) {
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/questions/delete/" + questionId)).build();
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
     * Post question question.
     *
     * @param question the question
     * @return the question
     */
    public static Question postQuestion(Question question) {
        String requestBody = gson.toJson(question);
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/questions/insert")).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Question.class);
    }

    /**
     * Changes the content of the question with the requested body.
     *
     * @param questionId the question id
     * @param content    the content
     * @return the string
     */
    public static String rephraseQuestion(long questionId, String content) {
        //String requestBody = gson.toJson(content);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .uri(URI.create("http://localhost:8080/questions/rephrase/" + questionId)).build();
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
        return gson.fromJson(response.body(), StringResponse.class).getResponse();
    }

    /**
     * Sets the answer id of the question.
     *
     * @param questionId the question id
     * @param answerId   the answer id
     * @return question answer id
     */
    public static Question setAnswerId(long questionId, long answerId) {
        String requestBody = gson.toJson(answerId);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/questions/setAnswerId/" + questionId)).build();
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
        return gson.fromJson(response.body(), Question.class);
    }

}
