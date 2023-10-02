package users.middleware;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The class AuthenticationInterceptorRegistryTest.
 */
public class AuthenticationInterceptorRegistryTest {

    /**
     * The Registry.
     */
    AuthenticationInterceptorRegistry registry = new AuthenticationInterceptorRegistry();

    /**
     * Test that the authentication interceptor returns a non-null result.
     */
    @Test
    public void authenticationInterceptor_ReturnsANonNullResult() {
        AuthenticationInterceptor interceptor = registry.authenticationInterceptor();
        Assertions.assertNotNull(interceptor);
    }
}
