package reservations.middleware;

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
import reservations.clients.AuthenticationClient;

@ExtendWith(MockitoExtension.class)
public class AuthenticationInterceptorTest {
    @Mock
    AuthenticationClient authenticationClient;

    @InjectMocks
    AuthenticationInterceptor interceptor;

    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    String authorizationHeaderName = "Authorization";
    String testJwt = "Bearer"
            + " eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0MDkyMDIyOSwiaWF0IjoxNjQwODg0MjI5fQ"
            + ".Yk04Bhqy-gZOEty5eap8RZZk-Y4gEHk7XnmxGUcepVE";

    StringBuffer userAccessibleUrl = new StringBuffer("http://localhost:9091/api/v1/facility/book");
    StringBuffer addFacilityUrl = new StringBuffer("http://localhost:9091/api/v1/facility/add");
    StringBuffer removeFacilityUrl = new StringBuffer("http://localhost:9091/api/v1/facility/delete");
    StringBuffer updateFacilityUrl = new StringBuffer("http://localhost:9091/api/v1/facility/update");
    StringBuffer getByBookableUrl = new StringBuffer("http://localhost:9091/api/v1/reservation/getByBookable");
    StringBuffer bypassAuthenticationUrl =
            new StringBuffer("http://localhost:9091/api/v1/reservation/exists");

    String userRole = "USER";

    @Test
    public void interceptor_ReturnsTrue_WhenAuthenticationShouldBeBypassed() throws IOException {
        when(request.getRequestURL()).thenReturn(bypassAuthenticationUrl);
        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    @Test
    public void interceptor_ReturnsTrue_WhenCredentialsCorrect() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    @Test
    public void interceptor_ReturnsFalse_WhenJwtIsNull() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(null);

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsFalse_WhenNoPermissionsReturned() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of("NONE")));

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsFalse_WhenUserTriesToAccessAddEndpoint() throws IOException {
        when(request.getRequestURL()).thenReturn(addFacilityUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsFalse_WhenUserTriesToAccessUpdateEndpoint() throws IOException {
        when(request.getRequestURL()).thenReturn(updateFacilityUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsFalse_WhenUserTriesToAccessDeleteEndpoint() throws IOException {
        when(request.getRequestURL()).thenReturn(removeFacilityUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsFalse_WhenUserTriesToAccessGetReservationsByBookable()
            throws IOException {
        when(request.getRequestURL()).thenReturn(getByBookableUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsTrue_WhenUserTriesToAccessNormalEndpoint() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of(userRole)));

        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    @Test
    public void interceptor_ReturnsFalse_WhenUnidentifiableRoleReturned() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of("FOO")));

        performExpectedUnauthorizedAssertion();
    }

    @Test
    public void interceptor_ReturnsTrue_WhenAdminRoleReturned() throws IOException {
        when(request.getRequestURL()).thenReturn(userAccessibleUrl);
        when(request.getHeader(authorizationHeaderName)).thenReturn(testJwt);
        when(authenticationClient.permit(testJwt))
                .thenReturn(ResponseEntity.of(Optional.of("ADMIN")));

        Assertions.assertTrue(interceptor.preHandle(request, response, 1));
    }

    private void performExpectedUnauthorizedAssertion() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        Assertions.assertFalse(interceptor.preHandle(request, response, 1));

        String result = stringWriter.getBuffer().toString().trim();
        Assertions.assertEquals("You are not authorized to perform this action.", result);
    }

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
