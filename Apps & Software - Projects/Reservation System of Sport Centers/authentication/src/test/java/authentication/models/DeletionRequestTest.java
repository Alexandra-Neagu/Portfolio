package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class DeletionRequestTest {

    private String password = "password";

    @Test
    public void emptyConstructor_ShouldBeNonNull() {
        DeletionRequest deletionRequest = new DeletionRequest();
        assertNotEquals("deletionRequest should be non null",
                null, deletionRequest);
    }

    @Test
    public void nonEmptyConstructor_ShouldBeNonNull() {
        DeletionRequest deletionRequest = new DeletionRequest(password);
        assertNotEquals("deletionRequest should be non null",
                null, deletionRequest);
    }

    @Test
    public void getters_ShouldWork_WhenCalledCorrectly() {
        DeletionRequest deletionRequest = new DeletionRequest(password);
        assertEquals("password should be get correctly",
                password, deletionRequest.getPassword());
    }
    
    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        DeletionRequest request = new DeletionRequest(password);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        DeletionRequest request = new DeletionRequest(password);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        DeletionRequest request = new DeletionRequest(password);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentPassword() {
        DeletionRequest request = new DeletionRequest(password);
        DeletionRequest request2 = new DeletionRequest("a");

        assertEquals("Comparing to different password should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        DeletionRequest request = new DeletionRequest(password);
        DeletionRequest request2 = new DeletionRequest(
                "password");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        DeletionRequest request = new DeletionRequest(password);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(password), request.hashCode());
    }
}
