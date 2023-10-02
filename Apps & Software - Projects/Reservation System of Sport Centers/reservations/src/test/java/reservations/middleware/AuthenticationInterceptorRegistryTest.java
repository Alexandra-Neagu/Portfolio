package reservations.middleware;

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
     * Authentication interceptor returns a non null result.
     */
    @Test
    public void authenticationInterceptor_ReturnsANonNullResult() {
        AuthenticationInterceptor interceptor = registry.authenticationInterceptor();
        Assertions.assertNotNull(interceptor);
    }
}
