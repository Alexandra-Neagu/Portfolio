package users.middleware;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import users.clients.AuthenticationClient;

/**
 * The class AuthenticationInterceptorTest.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationInterceptorTest {

    /**
     * The Authentication client.
     */
    @Mock
    AuthenticationClient authenticationClient;

    /**
     * The Interceptor.
     */
    @InjectMocks
    AuthenticationInterceptor interceptor;

    /**
     * The Request.
     */
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    /**
     * The Response.
     */
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    /**
     * The Authorization header name.
     */
    String authorizationHeaderName = "Authorization";

    /**
     * The Test jwt.
     */
    String testJwt = "Bearer"
            + " eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0MDkyMDIyOSwiaWF0IjoxNjQwODg0MjI5fQ"
            + ".Yk04Bhqy-gZOEty5eap8RZZk-Y4gEHk7XnmxGUcepVE";

    /**
     * The User accessible url.
     */
    StringBuffer userAccessibleUrl = new StringBuffer("http://localhost:9091/api/v1/facility/book");

    /**
     * The Add facility url.
     */
    StringBuffer addFacilityUrl = new StringBuffer("http://localhost:9091/api/v1/facility/add");

    /**
     * The Remove facility url.
     */
    StringBuffer removeFacilityUrl = new StringBuffer("http://localhost:9091/api/v1/facility/delete");

    /**
     * The Update facility url.
     */
    StringBuffer updateFacilityUrl = new StringBuffer("http://localhost:9091/api/v1/facility/update");

    /**
     * The Bypass authentication url.
     */
    StringBuffer bypassAuthenticationUrl =
            new StringBuffer("http://localhost:9091/api/v1/reservation/exists");

    /**
     * The User role.
     */
    String userRole = "USER";

    /**
     * Test that the interceptor returns true when authentication should be bypassed.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsTrue_WhenAuthenticationShouldBeBypassed() throws IOException {
        when(request.getRequestURL()).thenReturn(bypassAuthenticationUrl);
        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    /**
     * Test that the interceptor returns true when credentials correct.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsTrue_WhenCredentialsCorrect() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    /**
     * Test that the interceptor returns false when JWT is null.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsFalse_WhenJwtIsNull() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(null);

        performExpectedUnauthorizedAssertion();
    }

    /**
     * Test that the interceptor returns false when no permissions returned.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsFalse_WhenNoPermissionsReturned() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of("NONE")));

        performExpectedUnauthorizedAssertion();
    }

    /**
     * Test that the interceptor returns true when user tries to access normal endpoint.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsTrue_WhenUserTriesToAccessNormalEndpoint() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    /**
     * Test that the interceptor returns false when unidentifiable role returned.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsFalse_WhenUnidentifiableRoleReturned() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of("FOO")));

        performExpectedUnauthorizedAssertion();
    }

    /**
     * Test that the interceptor returns true when admin role returned.
     *
     * @throws IOException the io exception
     */
    @Test
    public void interceptor_ReturnsTrue_WhenAdminRoleReturned() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of("ADMIN")));

        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    /**
     * Performs expected unauthorized assertion.
     *
     * @throws IOException the io exception
     */
    private void performExpectedUnauthorizedAssertion() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        Assertions.assertFalse(interceptor.preHandle(request, response, 1));

        String result = stringWriter.getBuffer().toString().trim();
        Assertions.assertEquals("You are not authorized to perform this action.", result);
    }

    /**
     * Test that an unauthorized response sets correct result.
     *
     * @throws IOException the io exception
     */
    @Test
    public void unauthorizedResponse_SetsCorrectResult() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        interceptor.setUnauthorizedResponse(response);
        String result = stringWriter.getBuffer().toString().trim();
        Assertions.assertEquals("You are not authorized to perform this action.", result);

        int unauthorizedStatusCode = 401;
        verify(response, times(1)).setStatus(unauthorizedStatusCode);
    }

}
