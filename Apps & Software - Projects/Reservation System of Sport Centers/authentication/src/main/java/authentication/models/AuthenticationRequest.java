package authentication.models;

import java.util.Objects;

/**
 * Authentication request.
 */
public class AuthenticationRequest {

    private String username;
    private String password;

    /**
     * Checks if two AuthenticationRequest are equal.
     *
     * @param o the AuthenticationRequest to compare to.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticationRequest)) {
            return false;
        }
        AuthenticationRequest that = (AuthenticationRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    /**
     *  Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    /**
     * Empty constructor.
     */
    public AuthenticationRequest() {
    }

    /**
     * Constructor.
     *
     * @param username the username
     * @param password the password which is encoded
     */
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Setter of the username.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter of the password.
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter of the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter of the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
