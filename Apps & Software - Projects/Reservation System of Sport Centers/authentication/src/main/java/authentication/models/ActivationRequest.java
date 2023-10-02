package authentication.models;

import java.util.Objects;

/**
 * First admin account activation request.
 */
public class ActivationRequest {

    private String activationKey;
    private String password;
    private String password2;

    /**
     * Checks if two ActivationRequest are equal.
     *
     * @param o the ActivationRequest to compare to.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivationRequest)) {
            return false;
        }
        ActivationRequest that = (ActivationRequest) o;
        return Objects.equals(activationKey, that.activationKey)
                && Objects.equals(password, that.password)
                && Objects.equals(password2, that.password2);
    }

    /**
     *  Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(activationKey, password, password2);
    }

    /**
     * Empty constructor.
     */
    public ActivationRequest() {}

    /**
     * Constructor.
     *
     * @param activationKey the activation key
     * @param password the password
     * @param password2 the verification password
     */
    public ActivationRequest(String activationKey, String password, String password2) {
        this.activationKey = activationKey;
        this.password = password;
        this.password2 = password2;
    }

    /**
     * Getter of the activation key.
     *
     * @return the activation key
     */
    public String getActivationKey() {
        return activationKey;
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
