package authentication.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import authentication.clients.UsersClient;
import authentication.entities.UserCredential;
import authentication.models.ActivationRequest;
import authentication.models.AdminRequest;
import authentication.models.DeletionRequest;
import authentication.models.PasswordRequest;
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
public class FilteredAuthenticateServiceTest {

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
    private FilteredAuthenticationService authenticateService;


    private final String activationKey = "ACTIVATE";
    private final String adminRole = "ADMIN";
    private final String userRole = "USER";
    private final String none = "NONE";
    private final JwtUtil jwtUtil = new JwtUtil();
    private final String username = "Luuk";
    private final String adminUsername = "Admin Luuk";
    private final String newAdminName = "newAdmin@NameName";
    private final String password = "password";
    private final String newPassword = "VeryGoodNewPasswordHasArrived";
    private final UserCredential admin = new UserCredential(adminUsername, password, adminRole);
    private final UserCredential user = new UserCredential(username, password, userRole);
    private final String validUserToken = "Bearer " + jwtUtil.generateToken(user);
    private final String validAdminToken = "Bearer " + jwtUtil.generateToken(admin);
    private final ActivationRequest activateReq =
            new ActivationRequest(activationKey, password, password);

    @Test
    public void addAdmin_ThrowsException_WhenUserTriesToAccess() {
        AdminRequest request = new AdminRequest(adminUsername, password, password);
        String token = validAdminToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(adminUsername);
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(user);
        assertThrows(
                IllegalAccessException.class,
                () -> authenticateService.addAdmin(validAdminToken, request)
        );

        verify(jwtUtilToken, times(1))
                .extractUsername(token);
        verify(myUserDetailService, times(1))
                .loadUserByUsername(adminUsername);
    }

    @Test
    public void addAdmin_ThrowsException_WhenUsernameIsInUse() {
        AdminRequest request = new AdminRequest(adminUsername, password, password);
        String token = validAdminToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(adminUsername);
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(admin);

        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.addAdmin(validAdminToken, request)
        );


        verify(jwtUtilToken, times(1))
                .extractUsername(token);
        verify(myUserDetailService, times(2))
                .loadUserByUsername(adminUsername);
    }

    @Test
    public void addAdmin_ThrowsException_WhenItIsNoEmail() {
        AdminRequest request = new AdminRequest(
                adminUsername + "2", password, password);
        String token = validAdminToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(adminUsername);
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(admin);

        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.addAdmin(validAdminToken, request)
        );


        verify(jwtUtilToken, times(1))
                .extractUsername(token);
        verify(myUserDetailService, times(1))
                .loadUserByUsername(adminUsername);
    }

    @Test
    public void addAdmin_ThrowsException_WhenPasswordDontMatch() {
        AdminRequest request = new AdminRequest(
                newAdminName, password, password + "2");
        String token = validAdminToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(adminUsername);
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(admin);

        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.addAdmin(validAdminToken, request)
        );


        verify(jwtUtilToken, times(1))
                .extractUsername(token);
        verify(myUserDetailService, times(1))
                .loadUserByUsername(adminUsername);
    }

    @Test
    public void addAdmin_WorksCorrectly_WhenCalledCorrectly() throws IllegalAccessException {
        String token = validAdminToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(adminUsername);
        when(myUserDetailService.loadUserByUsername(adminUsername))
                .thenReturn(admin);
        when(myUserDetailService.loadUserByUsername(newAdminName))
                .thenThrow(new UsernameNotFoundException(""));
        AdminRequest request = new AdminRequest(
                newAdminName, password, password);

        authenticateService.addAdmin(validAdminToken, request);

        verify(jwtUtilToken, times(1))
                .extractUsername(token);
        verify(myUserDetailService, times(1))
                .loadUserByUsername(adminUsername);
        verify(myUserDetailService, times(1))
                .addUserCredential(any());
    }

    @Test
    public void changePasswordCorrectly_Assignment3() {
        String token = validUserToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(username);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(null);
        PasswordRequest request =
                new PasswordRequest(password, newPassword, newPassword);
        String encodedPassword = authenticateService.encoder.encode(newPassword);
        UserCredential newUser = user;
        newUser.setPassword(encodedPassword);

        authenticateService.changePasswordUser(validUserToken, request);

        verify(myUserDetailService, times(1)).deleteUserCredential(user);
        verify(myUserDetailService, times(1)).addUserCredential(newUser);

        verify(authenticationManager, times(2))
                .authenticate(any());
        verify(myUserDetailService, times(2))
                .loadUserByUsername(username);
    }

    @Test
    public void changePasswordIncorrectly_Assignment3() {
        String token = validUserToken.substring(7);
        final String encodedPassword = authenticateService.encoder.encode(newPassword);
        UserCredential newUser = new UserCredential(username, encodedPassword, userRole);

        when(jwtUtilToken.extractUsername(token))
                .thenReturn(username);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);
        when(authenticationManager.authenticate(any()))
                .thenReturn(null)
                .thenThrow(new BadCredentialsException("Incorrect username or password."));

        PasswordRequest request =
                new PasswordRequest(password, newPassword, newPassword);

        assertThrows(
                BadCredentialsException.class,
                () -> authenticateService.changePasswordUser(
                        validUserToken, request)
        );

        verify(myUserDetailService, times(2))
                .deleteUserCredential(any());
        verify(myUserDetailService, times(2))
                .addUserCredential(any());
        verify(authenticationManager, times(2))
                .authenticate(any());
        verify(myUserDetailService, times(2))
                .loadUserByUsername(username);
    }

    @Test
    public void changePassword_ThrowsException_WhenPasswordIsWrong() {
        PasswordRequest request =
                new PasswordRequest(password, newPassword, password);
        assertThrows(
                InvalidParameterException.class,
                () -> authenticateService.changePasswordUser(
                        validUserToken, request)
        );
    }

    @Test
    public void changePassword_ThrowsException_WhenCredentialsAreWrong() {
        String token = validUserToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(username);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenThrow(new BadCredentialsException("Incorrect username or password."));
        PasswordRequest request =
                new PasswordRequest(password, newPassword, newPassword);

        assertThrows(
                BadCredentialsException.class,
                () -> authenticateService.changePasswordUser(
                        validUserToken, request)
        );
        verify(authenticationManager, times(1))
                .authenticate(any());
        verify(myUserDetailService, times(2))
                .loadUserByUsername(username);
    }

    @Test
    public void deleteUser_ThrowsException_WhenAuthenticationIsIncorrect() {
        DeletionRequest request = new DeletionRequest(password);
        String token = validUserToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(username);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenThrow(new BadCredentialsException("Incorrect username or password."));

        assertThrows(
                BadCredentialsException.class,
                () -> authenticateService.deleteUser(
                        validUserToken, request)
        );

        verify(jwtUtilToken, times(1)).extractUsername(token);
        verify(authenticationManager, times(1))
                .authenticate(any());

    }

    @Test
    public void deleteUser_WorksCorrectlyOnUser_WithCorrectInputs() {
        String token = validUserToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(username);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(authentication);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(user);
        DeletionRequest request = new DeletionRequest(password);

        authenticateService.deleteUser(validUserToken, request);

        verify(jwtUtilToken, times(1)).extractUsername(token);
        verify(authenticationManager, times(1))
                .authenticate(any());
        verify(myUserDetailService, times(1))
                .loadUserByUsername(username);
        verify(myUserDetailService, times(1))
                .deleteUserCredential(any());
        verify(usersClient, times(1)).deleteUser(any());
    }

    @Test
    public void deleteUser_WorksCorrectlyOnAdmin_WithCorrectInputs() {
        String token = validAdminToken.substring(7);
        when(jwtUtilToken.extractUsername(token))
                .thenReturn(username);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(authentication);
        when(myUserDetailService.loadUserByUsername(username))
                .thenReturn(admin);
        DeletionRequest request = new DeletionRequest(password);

        authenticateService.deleteUser(validAdminToken, request);

        verify(jwtUtilToken, times(1)).extractUsername(token);
        verify(authenticationManager, times(1))
                .authenticate(any());
        verify(myUserDetailService, times(1))
                .loadUserByUsername(username);
        verify(myUserDetailService, times(1))
                .deleteUserCredential(any());
    }
}
