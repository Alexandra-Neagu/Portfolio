package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class ActivationRequestTest {

    private String activationKey = "actionvationKey";
    private String password = "password";
    private String password2 = "password2";

    @Test
    public void emptyConstructor_ShouldBeNonNull() {
        ActivationRequest activationRequest = new ActivationRequest();
        assertNotEquals("activationRequest should be non null",
                null, activationRequest);
    }

    @Test
    public void nonEmptyConstructor_ShouldBeNonNull() {
        ActivationRequest activationRequest = new ActivationRequest(
                activationKey, password, password2);
        assertNotEquals("activationRequest should be non null",
                null, activationRequest);
    }

    @Test
    public void getters_ShouldWork_WhenCalledCorrectly() {
        ActivationRequest activationRequest = new ActivationRequest(
                activationKey, password, password2);
        assertEquals("activationKey should be get correctly",
                activationKey, activationRequest.getActivationKey());
        assertEquals("password should be get correctly",
                password, activationRequest.getPassword());
        assertEquals("password2 should be get correctly",
                password2, activationRequest.getPassword2());
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentKey() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        ActivationRequest request2 = new ActivationRequest("a", password, password2);

        assertEquals("Comparing to different key should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        ActivationRequest request2 = new ActivationRequest(activationKey, "a", password2);

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword2() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        ActivationRequest request2 = new ActivationRequest(activationKey, password, "a");

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        ActivationRequest request2 = new ActivationRequest(
                "actionvationKey", "password", "password2");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(activationKey, password, password2), request.hashCode());
    }
}
