package authentication.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import authentication.entities.UserCredential;
import authentication.models.ActivationRequest;
import authentication.models.AdminRequest;
import authentication.models.AuthenticationRequest;
import authentication.models.PasswordRequest;
import authentication.models.RegistrationRequest;
import authentication.services.AuthenticateService;
import authentication.services.FilteredAuthenticationService;
import authentication.services.MyUserDetailService;
import authentication.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.Charset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = AuthenticationAPI.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationAPITest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticateService authenticateService;

    @MockBean
    FilteredAuthenticationService filteredAuthenticationService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    MyUserDetailService myUserDetailService;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    private static String name = "Luuk";
    private static String username = "lwlvandelaar@student.tudelft.nl";
    private static String password = "VerySecurePassword";
    private static String password2 = "VerySecurePassword";
    private static String jwtTokenUser;
    private static String jwtTokenAdmin;
    String activationKey = "KeyToActivateOurBeautifulProgram";
    String baseURL = "/api/v1/authentication";
    String registerURL = "/register";
    String activateURL = "/activate";
    String passURL = "/change-password";
    String newPassword2 = "newVerySecurePassword";
    String newPassword = "newVerySecurePassword";
    String authorizationHeader = "Bearer 4db59550-7b5d-11ec-90d6-0242ac120003";
    String authorizationHeaderName = "Authorization";

    //Sadly we couldn't test all the methods as a lot of them are
    //dependent on the protected filter class
    //which we can not mock easily. Therefore we have to sadly inform
    // you that these methods are not tested.

    /**
     * This method is executed before all tests.
     */
    @BeforeAll
    public static void init() {
        JwtUtil jwt = new JwtUtil();
        jwtTokenUser = jwt.generateToken(new UserCredential(name, password, "USER"));
        jwtTokenAdmin = jwt.generateToken(new UserCredential(name, password, "ADMIN"));
    }

    @Test
    public void adminRegistration_ThrowsException_WhenUsernameNull() throws Exception {
        AdminRequest request = new AdminRequest(null, password, password2);
        adminRegistration_ThrowsException(request);
    }

    @Test
    public void adminRegistration_ThrowsException_WhenPasswordNull() throws Exception {
        AdminRequest request = new AdminRequest(username, null, password2);
        adminRegistration_ThrowsException(request);
    }

    @Test
    public void adminRegistration_ThrowsException_WhenPassword2Null() throws Exception {
        AdminRequest request = new AdminRequest(username, password, null);
        adminRegistration_ThrowsException(request);
    }

    private void adminRegistration_ThrowsException(AdminRequest request) throws Exception {
        String requestJson = jsonRequest(request);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(baseURL + "/add-admin")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)
                .header(authorizationHeaderName, authorizationHeader);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

        verify(filteredAuthenticationService, never()).addAdmin(authorizationHeader, request);
    }

    @Test
    public void adminRegistration_SuccessfullyRegisters_WhenParametersCorrect() throws Exception {
        AdminRequest request = new AdminRequest(username, password, password2);
        String requestJson = jsonRequest(request);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(baseURL + "/add-admin")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)
                .header(authorizationHeaderName, authorizationHeader);

        String result = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals("The admin account has been created.", result);
        verify(filteredAuthenticationService, times(1)).addAdmin(authorizationHeader, request);
    }

    @Test
    public void userRegistration_BehavesCorrectly_WithCorrectInputs() throws Exception {
        RegistrationRequest request = new RegistrationRequest(name, username, password, password2);
        String requestJson = jsonRequest(request);

        mockMvc.perform(post(baseURL + registerURL).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());

        verify(authenticateService, times(1)).registerUser(request);
    }

    @Test
    public void permit_BehavesCorrectly_WithCorrectInputs() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(baseURL + "/permit")
                .param("jwt", jwtTokenAdmin);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(authenticateService, times(1))
                .getPermissions(jwtTokenAdmin, jwtUtil);
    }

    @Test
    public void authenticate_BehavesCorrectly_WithCorrectInputs() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + "/authenticate").contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());

        verify(authenticateService, times(1))
                .authenticate(request);
    }

    @Test
    public void activate_BehavesCorrectly_WithCorrectInputs() throws Exception {
        ActivationRequest request = new ActivationRequest(activationKey, password, password2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + activateURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());

        verify(authenticateService, times(1))
                .activation(request);
    }

    @Test
    public void activate_ThrowsException_WithIncorrectInputPassword2() throws Exception {
        ActivationRequest request = new ActivationRequest(activationKey, null, password2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + activateURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .activation(request);
    }

    @Test
    public void activate_ThrowsException_WithIncorrectInputPassword() throws Exception {
        ActivationRequest request = new ActivationRequest(activationKey, password, null);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + activateURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .activation(request);
    }

    @Test
    public void activate_ThrowsException_WithIncorrectInputActivationKey() throws Exception {
        ActivationRequest request = new ActivationRequest(null, password, password2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + activateURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .activation(request);
    }


    @Test
    public void authenticate_ThrowsException_WithIncorrectPassword() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(username, null);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + "/authenticate").contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .authenticate(request);
    }

    @Test
    public void authenticate_ThrowsException_WithIncorrectUsername() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(null, password);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + "/authenticate").contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .authenticate(request);
    }

    @Test
    public void userRegister_ThrowsException_WithIncorrectPassword2() throws Exception {
        RegistrationRequest request = new RegistrationRequest(name, username,  password, null);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + registerURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .registerUser(request);
    }

    @Test
    public void userRegister_ThrowsException_WithIncorrectPassword() throws Exception {
        RegistrationRequest request = new RegistrationRequest(name, username,  null, password2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + registerURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .registerUser(request);
    }

    @Test
    public void userRegister_ThrowsException_WithIncorrectUsername() throws Exception {
        RegistrationRequest request = new RegistrationRequest(name, null,  password, password2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + registerURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .registerUser(request);
    }

    @Test
    public void userRegister_ThrowsException_WithIncorrectName() throws Exception {
        RegistrationRequest request = new RegistrationRequest(null, username,  password, password2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + registerURL).contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(authenticateService, times(0))
                .registerUser(request);
    }

    @Test
    public void changePassword_ThrowsException_WithIncorrectPassword() throws Exception {
        PasswordRequest request = new PasswordRequest(null, newPassword, newPassword2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + passURL).contentType(APPLICATION_JSON_UTF8)
                .content(json)
                .header(authorizationHeaderName, authorizationHeader))
                .andExpect(status().isBadRequest());

        verify(filteredAuthenticationService, times(0))
                .changePasswordUser(authorizationHeader, request);
    }

    @Test
    public void changePassword_ThrowsException_WithIncorrectNewPassword() throws Exception {
        PasswordRequest request = new PasswordRequest(password, null, newPassword2);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + passURL).contentType(APPLICATION_JSON_UTF8)
                .content(json)
                .header(authorizationHeaderName, authorizationHeader))
                .andExpect(status().isBadRequest());

        verify(filteredAuthenticationService, times(0))
                .changePasswordUser(authorizationHeader, request);
    }

    @Test
    public void changePassword_ThrowsException_WithIncorrectNewPassword2() throws Exception {
        PasswordRequest request = new PasswordRequest(password, newPassword, null);
        String json = jsonRequest(request);

        mockMvc.perform(post(baseURL + passURL).contentType(APPLICATION_JSON_UTF8)
                .content(json)
                .header(authorizationHeaderName, authorizationHeader))
                .andExpect(status().isBadRequest());

        verify(filteredAuthenticationService, times(0))
                .changePasswordUser(authorizationHeader, request);
    }

    @Test
    public void changePassword_WorksCorrectly_WithCorrectInputs() throws Exception {
        PasswordRequest request = new PasswordRequest(password, newPassword, newPassword2);
        String json = jsonRequest(request);

        String result = mockMvc.perform(post(baseURL + passURL).contentType(APPLICATION_JSON_UTF8)
                .content(json)
                .header(authorizationHeaderName, authorizationHeader))
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        verify(filteredAuthenticationService, times(1))
                .changePasswordUser(authorizationHeader, request);

        assertEquals("Your password has successfully changed.", result);
    }


    /**
     * Makes a json String out of a request.
     *
     * @param request object to made a json String of.
     * @return the json String.
     * @throws JsonProcessingException when it is not Json processable.
     */
    public String jsonRequest(Object request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);
        return requestJson;
    }


}
