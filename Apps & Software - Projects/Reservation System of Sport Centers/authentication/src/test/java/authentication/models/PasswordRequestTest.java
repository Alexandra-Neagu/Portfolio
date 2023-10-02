package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class PasswordRequestTest {

    private String password = "password";
    private String newPassword = "newPassword";
    private String newPassword2 = "newPassword2";

    @Test
    public void emptyConstructor_ShouldBeNonNull() {
        PasswordRequest passwordRequest = new PasswordRequest();
        assertNotEquals("PasswordRequest should be non null",
                null, passwordRequest);
    }

    @Test
    public void nonEmptyConstructor_ShouldBeNonNull() {
        PasswordRequest passwordRequest =
                new PasswordRequest(password, newPassword, newPassword2);
        assertNotEquals("PasswordRequest should be non null",
                null, passwordRequest);
    }

    @Test
    public void getters_ShouldWorkCorrectly_WhenCalled() {
        PasswordRequest passwordRequest =
                new PasswordRequest(password, newPassword, newPassword2);
        assertEquals("NewPassword should be get correctly",
                newPassword, passwordRequest.getNewPassword());
        assertEquals("NewPassword2 should be get correctly",
                newPassword2, passwordRequest.getNewPassword2());
        assertEquals("Password should be get correctly",
                password, passwordRequest.getPassword());
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentUsername() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        PasswordRequest request2 = new PasswordRequest("a", newPassword, newPassword2);

        assertEquals("Comparing to different key should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentNewPassword() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        PasswordRequest request2 = new PasswordRequest(password, "a", newPassword2);

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentNewPassword2() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        PasswordRequest request2 = new PasswordRequest(password, newPassword, "a");

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        PasswordRequest request2 = new PasswordRequest(
                "password", "newPassword", "newPassword2");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(password, newPassword, newPassword2), request.hashCode());
    }
}
