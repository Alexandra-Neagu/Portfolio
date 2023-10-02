package authentication.models;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class AuthenticationResponseTest {

    private String jwt = "jwt";

    @Test
    public void constructor_ShouldBeNonNull() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
        assertNotEquals("authenticationResponse should be non null",
                null, authenticationResponse);
    }

    @Test
    public void getJwtTest() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
        assertEquals("Jwt should be the correct jwt token",
                jwt, authenticationResponse.getJwt());
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSelf() {
        AuthenticationResponse request = new AuthenticationResponse(jwt);

        assertEquals("Comparing to itself should be true.",
                true, request.equals(request));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        AuthenticationResponse request = new AuthenticationResponse(jwt);
        Object nil = null;

        assertEquals("Comparing to null should be false.",
                false, request.equals(nil));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentClass() {
        AuthenticationResponse request = new AuthenticationResponse(jwt);
        List<String> list = new ArrayList<>();

        assertEquals("Comparing to other object should be false.",
                false, request.equals(list));
    }

    @Test
    public void equals_ReturnFalse_WhenComparedWithDifferentJWT() {
        AuthenticationResponse request = new AuthenticationResponse(jwt);
        AuthenticationResponse request2 = new AuthenticationResponse("a");

        assertEquals("Comparing to different JWT token should be false.",
                false, request.equals(request2));
    }

    @Test
    public void equals_ReturnTrue_WhenComparedWithEqual() {
        AuthenticationResponse request = new AuthenticationResponse(jwt);
        AuthenticationResponse request2 = new AuthenticationResponse("jwt");

        assertEquals("Comparing to equal should be true.",
                true, request.equals(request2));
    }

    @Test
    public void hash_ReturnsCorrectHash() {
        AuthenticationResponse request = new AuthenticationResponse(jwt);
        assertEquals("Hash should hash correctly like the object hash",
                Objects.hash(jwt), request.hashCode());
    }
}
