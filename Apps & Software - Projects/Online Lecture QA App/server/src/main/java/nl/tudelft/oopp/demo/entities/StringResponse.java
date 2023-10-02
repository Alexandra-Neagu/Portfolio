package nl.tudelft.oopp.demo.entities;

import java.util.Objects;

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

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StringResponse)) {
            return false;
        }
        StringResponse that = (StringResponse) o;
        return Objects.equals(response, that.response);
    }

    @Override
    public String toString() {
        return response;
    }
}
