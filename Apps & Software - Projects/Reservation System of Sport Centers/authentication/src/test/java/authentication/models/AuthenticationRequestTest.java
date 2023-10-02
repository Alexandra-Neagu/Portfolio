package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class AuthenticationRequestTest {

    private String username = "username";
    private String password = "password";

    @Test
    public void emptyConstructor_ShouldBeNonNull() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        assertNotEquals("authenticationRequest should be non null",
                null, authenticationRequest);
    }

    @Test
    public void nonEmptyConstructor_ShouldBeNonNull() {
        AuthenticationRequest authenticationRequest =
                new AuthenticationRequest(username, password);
        assertNotEquals("authenticationRequest should be non null",
                null, authenticationRequest);
    }

    @Test
    public void gettersAndSetter_ShouldWorkCorrectly_WhenCalled() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);
        assertEquals("Username should be set correctly",
                username, authenticationRequest.getUsername());
        assertEquals("Password should be set correctly",
                password, authenticationRequest.getPassword());
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentUsername() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        AuthenticationRequest request2 = new AuthenticationRequest("a", password);

        assertEquals("Comparing to different key should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        AuthenticationRequest request2 = new AuthenticationRequest(username, "a");

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        AuthenticationRequest request2 = new AuthenticationRequest(
                "username", "password");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(username, password), request.hashCode());
    }
}
