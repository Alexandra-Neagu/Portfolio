package nl.tudelft.oopp.demo.communication;

// a class whose sole purpose is to wrap
// a String into an Object so it can be easily dealt with by JSON

/**
 * The type String response.
 */
public class StringResponse {

    private String response;

    /**
     * Instantiates a new String response.
     *
     * @param response the response
     */
    public StringResponse(String response) {
        this.response = response;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public String getResponse() {
        return response;
    }
}
