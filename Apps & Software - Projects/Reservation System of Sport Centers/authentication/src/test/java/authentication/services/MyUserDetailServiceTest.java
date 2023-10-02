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
import authentication.models.AdminRequest;
import authentication.models.AuthenticationRequest;
import authentication.models.DeletionRequest;
import authentication.models.PasswordRequest;
import authentication.models.RegistrationRequest;
import authentication.repositories.UserCredentialRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class MyUserDetailServiceTest {
    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private UsersClient usersClient;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtil jwtUtilToken;

    @Mock
    private PasswordEncoder wrongEncoder;

    @Autowired
    @InjectMocks
    private MyUserDetailService myUserDetailService;

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


    public MyUserDetailServiceTest() {
    }


    @Test
    public void loadUserByUsername_LoadsSuccessfully_WhenValuesAreValid() {
        when(userCredentialRepository.findByUsername(username))
                .thenReturn(user);
        UserCredential result = myUserDetailService.loadUserByUsername(username);

        assertEquals("Different userCredentials then the one required to return",
                user,
                result);

        verify(userCredentialRepository, times(1)).findByUsername(username);
    }

    @Test
    public void loadUserByUserName_ThrowsException_WhenUserDoesntExist() {
        when(userCredentialRepository.findByUsername(username))
                .thenReturn(null);
        assertThrows(
                UsernameNotFoundException.class,
                () -> myUserDetailService.loadUserByUsername(username)
        );

        verify(userCredentialRepository, times(1)).findByUsername(username);
    }

    @Test
    public void addUserCredential_AddUserCorrectly() {
        myUserDetailService.addUserCredential(user);
        verify(userCredentialRepository, times(1)).save(user);
    }

    @Test
    public void deleteUserCredential_DeletesUserCorrectly() {
        myUserDetailService.deleteUserCredential(user);
        verify(userCredentialRepository, times(1)).delete(user);
    }


}
