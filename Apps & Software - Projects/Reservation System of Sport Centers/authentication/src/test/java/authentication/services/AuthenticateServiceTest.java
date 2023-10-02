package authentication.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import authentication.clients.UsersClient;
import authentication.entities.UserCredential;
import authentication.models.ActivationRequest;
import authentication.models.AuthenticationRequest;
import authentication.models.RegistrationRequest;
import authentication.util.JwtUtil;
import java.security.InvalidParameterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceTest {
    @Mock
    private UsersClient usersClient;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MyUserDetailService myUserDetailService;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtil jwtUtilToken;

    @Autowired
    @InjectMocks
    private AuthenticateService authenticateService;


    private final String activationKey = "ACTIVATE";
    private final String adminRole = "ADMIN";
    private final String userRole = "USER";
    private final String none = "NONE";
    private final JwtUtil jwtUtil = new JwtUtil();
    private final String username = "Luuk";
    private final String adminUsername = "Admin Luuk";
    private final String password = "password";
    private final UserCredential admin = new UserCredential(adminUsername, password, adminRole);
    private final UserCredential user = new UserCredential(username, password, userRole);
    private final String validUserToken = "Bearer " + jwtUtil.generateToken(user);
    private final String validAdminToken = "Bearer " + jwtUtil.generateToken(admin);
    private final ActivationRequest activateReq =
            new ActivationRequest(activationKey, password, password);
    private final String newAdminName = "newAdmin@NameName";
    private final String newPassword = "VeryGoodNewPasswordHasArrived";

    @Test
    public void getPermissions_returnsNONE_WhenYouHaveNoPermissions() {
        assertEquals("Null has no permissions and should return NONE",
                none, authenticateService.getPermissions(null, jwtUtil));
    }

    @Test
    public void getPermissions_ReturnsNONE_WhenYouHaveNoBearerInFront() {
        String jwt = "TOKENIZE";
        assertEquals("Token should start with bearer ",
                none, authenticateService.getPermissions(jwt, jwtUtil));

    }

    @Test
    public void getPermissions_ReturnsNONE_WhenJwtHasNoUsername() {
        String jwt = "Bearer wrongToken";
        assertEquals("Token should have a valid username",
                none, authenticateService.getPermissions(jwt, jwtUtil));
    }

    @Test
    public void getPermissions_ReturnsAdmin_WhenAdminAskForPermission() {
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(admin);
        assertEquals("Token should have admin permissions",
                adminRole, authenticateService.getPermissions(validAdminToken, jwtUtil));
    }

    @Test
    public void getPermissions_ReturnsUser_WhenUserAskForPermission() {
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);
        assertEquals("Token should have user permissions",
                userRole, authenticateService.getPermissions(validUserToken, jwtUtil));
    }

    @Test
    public void getPermissions_ReturnsNONE_WhenTokenIsNotValid() {
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(null);
        assertEquals("Token should have no permissions",
                none, authenticateService.getPermissions(validAdminToken, jwtUtil));
    }

    @Test
    public void authenticate_ThrowsException_WhenUsernameAndPasswordAreIncorrect() {
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenThrow(new BadCredentialsException("Incorrect username or password."));
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        assertThrows(
                BadCredentialsException.class,
                () -> authenticateService.authenticate(request)
        );
    }

    @Test
    public void authenticate_ReturnsJwt_WhenUsernameAndPasswordAreCorrect() {
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(authentication);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);
        when(jwtUtilToken.generateToken(user)).thenReturn(validUserToken);
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        assertEquals("authenticate should return a jwtUtilToken",
                validUserToken, authenticateService.authenticate(request));

        verify(jwtUtilToken, times(1)).generateToken(user);
    }

    @Test
    public void activation_ThrowsException_WhenKeyIsNotRight() {
        ActivationRequest request =
                new ActivationRequest(activationKey + "wrong", password, password);
        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.activation(request)
        );
    }

    @Test
    public void activation_ThrowsException_WhenAdminHasAlreadyBeenMade() {
        when(myUserDetailService.loadUserByUsername("admin"))
                .thenReturn(admin);
        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.activation(activateReq)
        );
    }

    @Test
    public void activation_ThrowsException_WhenItHasDifferentPassword() {
        ActivationRequest request =
                new ActivationRequest(activationKey, password + "1", password);
        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.activation(request)
        );
    }

    @Test
    public void activation_WorksCorrectly_WhenCalledCorrectly() {
        when(myUserDetailService.loadUserByUsername("admin"))
                .thenThrow(new UsernameNotFoundException(""));
        authenticateService.activation(activateReq);
        verify(myUserDetailService, times(1))
                .addUserCredential(any());
    }


    @Test
    public void registerUser_ThrowsException_WhenUsernameIsInUse() {
        RegistrationRequest request =
                new RegistrationRequest(username, username, password, password);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);

        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.registerUser(request)
        );

        verify(myUserDetailService, times(1))
                .loadUserByUsername(username);
    }

    @Test
    public void registerUser_ThrowsException_WhenUsernameIsNoEmail() {
        RegistrationRequest request =
                new RegistrationRequest(username, username, password, password);
        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.registerUser(request)
        );
    }

    @Test
    public void registerUser_ThrowsException_WhenPasswordAreNotEqual() {
        RegistrationRequest request = new RegistrationRequest(
                username, newAdminName, password + "2", password);
        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.registerUser(request)
        );
    }

    @Test
    public void registerUser_WorksCorrectly_WithCorrectInputs() {
        when(myUserDetailService.loadUserByUsername(newAdminName))
                .thenThrow(new UsernameNotFoundException(""));

        RegistrationRequest request = new RegistrationRequest(
                username, newAdminName, password, password);

        authenticateService.registerUser(request);

        verify(usersClient, times(1)).addUser(any(), any());
        verify(myUserDetailService, times(1))
                .addUserCredential(any());
    }




}
