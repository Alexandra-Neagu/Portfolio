package authentication.filters;

import javax.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * The type JwtRequestFilter test class.
 */
public class JwtRequestFilterTest {

    @MockBean
    private MockHttpServletRequest request;

    @MockBean
    private MockHttpServletResponse response;

    @MockBean
    private FilterChain chain;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest("GET", "");
        request.addHeader("Authorization", "headerValue");

        response = new MockHttpServletResponse();
        chain = Mockito.mock(FilterChain.class);
    }

    /**
     * Tests that doFilter is called correctly by filterChain.
     *
     * @throws Exception the exception
     */
    @Test
    public void doFilterInternalTest() throws Exception {
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter();

        jwtRequestFilter.doFilterInternal(request, response, chain);

        Mockito.verify(chain, Mockito.times(1)).doFilter(request, response);
    }
}
