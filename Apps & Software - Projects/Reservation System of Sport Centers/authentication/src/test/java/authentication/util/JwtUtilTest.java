package authentication.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import authentication.entities.UserCredential;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

    private static String jwtToken;
    private static JwtUtil jwtUtil;
    private static UserCredential userCredential;
    private static String username = "foo";
    private static String password = "bar";

    /**
     * This method is executed before each test.
     */
    @BeforeAll
    public static void init() {
        userCredential = new UserCredential(username, password, "USER");
        jwtUtil = new JwtUtil();
        jwtToken = jwtUtil.generateToken(userCredential);
    }


    @Test
    public void generate_ReturnsToken() {
        assertNotEquals("jwtToken should not be null", null, jwtToken);
    }

    @Test
    public void validToken_WhenCredentialsAreEqual() {
        assertEquals("Token should be valid when credentials are equal",
                true, jwtUtil.validateToken(jwtToken, userCredential));
    }

    @Test
    public void notValidToken_WhenCredentialsAreNotEqual() {
        UserCredential falseCredential = new UserCredential("user", "pass", "role");
        assertEquals("Token should not be valid when credentials are not equal",
                false, jwtUtil.validateToken(jwtToken, falseCredential));
    }

    @Test
    public void equals_UsernameToken() {
        assertEquals("Username of token should be equal to the username",
                username, jwtUtil.extractUsername(jwtToken));
    }

    //Todo test different time inputs. I have no idea how to change the time.
}
