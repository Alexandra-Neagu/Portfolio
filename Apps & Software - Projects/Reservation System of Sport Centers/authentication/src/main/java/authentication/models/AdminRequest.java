package authentication.models;

import java.util.Objects;

public class AdminRequest {

    private String username;
    private String password;
    private String password2;

    /**
     * Checks if two AdminRequest are equal.
     *
     * @param o the AdminRequest to compare to.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminRequest)) {
            return false;
        }
        AdminRequest that = (AdminRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password)
                && Objects.equals(password2, that.password2);
    }

    /**
     * Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password, password2);
    }

    /**
     * Empty constructor.
     */
    public AdminRequest() {
    }

    /**
     * Constructor.
     *
     * @param username the username
     * @param password the password which is encoded
     */
    public AdminRequest(String username, String password, String password2) {
        this.username = username;
        this.password = password;
        this.password2 = password2;
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

    /**
     * Getter of the verification password.
     *
     * @return the verification password
     */
    public String getPassword2() {
        return password2;
    }
}
