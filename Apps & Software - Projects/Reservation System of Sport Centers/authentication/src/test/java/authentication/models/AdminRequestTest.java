package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class AdminRequestTest {

    private String username = "username";
    private String password = "password";
    private String password2 = "password2";

    @Test
    public void emptyConstructor_ShouldBeNonNull() {
        AdminRequest adminRequest = new AdminRequest();
        assertNotEquals("adminRequest should be non null",
                null, adminRequest);
    }

    @Test
    public void nonEmptyConstructor_ShouldBeNonNull() {
        AdminRequest adminRequest = new AdminRequest(username, password, password2);
        assertNotEquals("adminRequest should be non null",
                null, adminRequest);
    }

    @Test
    public void getters_ShouldWork_WhenCalledCorrectly() {
        AdminRequest adminRequest = new AdminRequest(username, password, password2);
        assertEquals("username should be get correctly",
                username, adminRequest.getUsername());
        assertEquals("password should be get correctly",
                password, adminRequest.getPassword());
        assertEquals("password2 should be get correctly",
                password2, adminRequest.getPassword2());
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        AdminRequest request = new AdminRequest(username, password, password2);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        AdminRequest request = new AdminRequest(username, password, password2);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        AdminRequest request = new AdminRequest(username, password, password2);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentUsername() {
        AdminRequest request = new AdminRequest(username, password, password2);
        AdminRequest request2 = new AdminRequest("a", password, password2);

        assertEquals("Comparing to different key should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword() {
        AdminRequest request = new AdminRequest(username, password, password2);
        AdminRequest request2 = new AdminRequest(username, "a", password2);

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword2() {
        AdminRequest request = new AdminRequest(username, password, password2);
        AdminRequest request2 = new AdminRequest(username, password, "a");

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        AdminRequest request = new AdminRequest(username, password, password2);
        AdminRequest request2 = new AdminRequest(
                "username", "password", "password2");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        AdminRequest request = new AdminRequest(username, password, password2);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(username, password, password2), request.hashCode());
    }
}
