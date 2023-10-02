package users.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import users.clients.AuthenticationClient;
import users.entities.User;
import users.middleware.AuthenticationInterceptor;
import users.services.UserService;

/**
 * The type User api test.
 */
@WebMvcTest(value = UserAPI.class)
public class UserAPITest {
    /**
     * The Mock mvc.
     */
    @Autowired
    MockMvc mockMvc;

    /**
     * The User service.
     */
    @MockBean
    UserService userService;

    /**
     * The Authentication interceptor.
     */
    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    /**
     * The Authentication client.
     */
    @MockBean
    AuthenticationClient authenticationClient;

    /**
     * The User.
     */
    User user;

    /**
     * The User name.
     */
    String userName = "john";

    /**
     * The User api url.
     */
    String userApiUrl = "/api/v1/user/";

    /**
     * The Uuid Parameter.
     */
    String uuidParameter = "userUuid";

    /**
     * The Uuid.
     */
    UUID uuid = UUID.fromString("12345678-1234-1234-1234-123456789012");

    /**
     * Setup.
     *
     * @throws IOException the io exception
     */
    @BeforeEach
    public void setup() throws IOException {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    /**
     * Test invalidParameterExceptionHandler.
     *
     * @throws Exception the exception
     */
    @Test
    public void testInvalidParameterExceptionHandler() throws Exception {
        when(userService.addUser(uuid, userName))
                .thenThrow(new InvalidParameterException("Exception"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(userApiUrl + "add")
                .param(uuidParameter, uuid.toString())
                .param("name", userName);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Exception"));
    }

    /**
     * Test addUser, behaves correctly.
     *
     * @throws Exception the exception
     */
    @Test
    public void addUser_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(userApiUrl + "add")
                .param(uuidParameter, uuid.toString())
                .param("name", userName);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
        
        verify(userService, times(1)).addUser(uuid, userName);
    }

    /**
     * Test changeSubscription.
     *
     * @throws Exception the exception
     */
    @Test
    public void changeSubscription() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(userApiUrl + "changeSubscription")
                .param(uuidParameter, uuid.toString())
                .param("hasPremium", "true");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(userService, times(1)).changeSubscription(uuid, true);
    }

    /**
     * Test deleteUser.
     *
     * @throws Exception the exception
     */
    @Test
    public void deleteUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(userApiUrl + "delete")
                .param(uuidParameter, uuid.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(null);
    }


}

