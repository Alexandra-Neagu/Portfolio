package authentication.models;

import java.util.Objects;

/**
 * Password changing request.
 */
public class PasswordRequest {

    private String password;
    private String newPassword;
    private String newPassword2;

    /**
     * Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(password, newPassword, newPassword2);
    }

    /**
     * Checks if two passwordRequests are equal.
     *
     * @param o the passwordRequest to compare to.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordRequest)) {
            return false;
        }
        PasswordRequest that = (PasswordRequest) o;
        return Objects.equals(password, that.password)
                && Objects.equals(newPassword, that.newPassword)
                && Objects.equals(newPassword2, that.newPassword2);
    }

    /**
     * Empty constructor.
     */
    public PasswordRequest() {}

    /**
     * Constructor.
     *
     * @param password the old/current password
     * @param newPassword the new password
     * @param newPassword2 verification of the new password
     */
    public PasswordRequest(String password, String newPassword, String newPassword2) {
        this.password = password;
        this.newPassword = newPassword;
        this.newPassword2 = newPassword2;
    }

    /**
     * Getter of the old/current password.
     *
     * @return the old/current password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter of the new password.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Getter of the verification of the new password.
     *
     * @return the new password verification
     */
    public String getNewPassword2() {
        return newPassword2;
    }
}
