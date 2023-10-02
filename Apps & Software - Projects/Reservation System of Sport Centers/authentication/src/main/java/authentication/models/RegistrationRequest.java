package authentication.models;

import java.util.Objects;

/**
 * Registration request.
 */
public class RegistrationRequest {

    private String name;
    private String username;
    private String password;
    private String password2;

    /**
     * Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, username, password, password2);
    }

    /**
     * Checks if to registrationsRequests are equal.
     *
     * @param o the regstartion request.
     * @return true if equals otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistrationRequest)) {
            return false;
        }
        RegistrationRequest that = (RegistrationRequest) o;
        return name.equals(that.name) && username.equals(that.username)
                && password.equals(that.password) && password2.equals(that.password2);
    }

    /**
     * Empty constructor.
     */
    public RegistrationRequest() {
    }

    /**
     * Constructor.
     *
     * @param username the username
     * @param password the password which is encoded
     */
    public RegistrationRequest(String name, String username, String password, String password2) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    /**
     * Getter of the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
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
