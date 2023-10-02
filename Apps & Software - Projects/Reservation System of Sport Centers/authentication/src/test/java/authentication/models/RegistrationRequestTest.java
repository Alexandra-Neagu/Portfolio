package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class RegistrationRequestTest {

    private String name = "name";
    private String username = "username";
    private String password = "password";
    private String password2 = "password2";

    @Test
    public void emptyConstructor_ShouldBeNonNull() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        assertNotEquals("registrationRequest should be non null",
                null, registrationRequest);
    }

    @Test
    public void nonEmptyConstructor_ShouldBeNonNull() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                name, username, password, password2);
        assertNotEquals("registrationRequest should be non null",
                null, registrationRequest);
    }

    @Test
    public void getters_ShouldWork_WhenCalledCorrectly() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                name, username, password, password2);
        assertEquals("Name should be get correctly",
                name, registrationRequest.getName());
        assertEquals("username should be get correctly",
                username, registrationRequest.getUsername());
        assertEquals("password should be get correctly",
                password, registrationRequest.getPassword());
        assertEquals("password2 should be get correctly",
                password2, registrationRequest.getPassword2());
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentName() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        RegistrationRequest request2 = new RegistrationRequest("a", username, password, password2);

        assertEquals("Comparing to different key should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentUsername() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        RegistrationRequest request2 = new RegistrationRequest(name, "a", password, password2);

        assertEquals("Comparing to different username should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        RegistrationRequest request2 = new RegistrationRequest(name, username, "a", password2);

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword2() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        RegistrationRequest request2 = new RegistrationRequest(name, username, password, "a");

        assertEquals("Comparing to different password2 should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        RegistrationRequest request2 = new RegistrationRequest(
                "name", "username", "password", "password2");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(name, username, password, password2), request.hashCode());
    }
}
