package authentication.models;

import java.util.Objects;

/**
 * Account deletion request.
 */
public class DeletionRequest {

    private String password;

    /**
     *  Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    /**
     * Checks if two DeletionRequest are equal.
     *
     * @param o the deletionRequest to compare to.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeletionRequest)) {
            return false;
        }
        DeletionRequest that = (DeletionRequest) o;
        return Objects.equals(password, that.password);
    }

    /**
     * Empty constructor.
     */
    public DeletionRequest() {}

    /**
     * Constructor.
     *
     * @param password the password
     */
    public DeletionRequest(String password) {
        this.password = password;
    }

    /**
     * Getter of the password.
     *
     * @return the old/current password
     */
    public String getPassword() {
        return password;
    }
}
