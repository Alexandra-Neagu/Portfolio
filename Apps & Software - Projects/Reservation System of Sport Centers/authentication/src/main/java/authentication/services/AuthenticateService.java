package authentication.services;

import authentication.clients.UsersClient;
import authentication.entities.UserCredential;
import authentication.models.ActivationRequest;
import authentication.models.AuthenticationRequest;
import authentication.models.RegistrationRequest;
import authentication.repositories.UserCredentialRepository;
import authentication.util.JwtUtil;
import java.security.InvalidParameterException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UsersClient usersClient;

    /*
    Each license of the software will come with a unique activation key.
    This activation key is required to create the first admin user (with
    username 'admin'), which is needed to create other admin users.

    So for clarification: "ACTIVATE" is just an example, actual activation
    keys will not consist out of guessable words, but will be randomly
    generated Strings.
    */

    final String activationKey = "ACTIVATE";
    final PasswordEncoder encoder = new BCryptPasswordEncoder();
    final String adminRole = "ADMIN";



    /**
     * Returns the role of a user based on their token.
     *
     * @param jwt The token to use
     * @param jwtUtil The utilities that will be used
     * @return A string, indicating the role of the user. Three option:
     *     - NONE: if there is no JW token present or it is incorrect,
     *     - USER: if the user is a regular user, and
     *     - ADMIN: if the user is an admin
     */
    public String getPermissions(String jwt, JwtUtil jwtUtil) {
        String username = null;

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                return "NONE";
            }
        }

        if (username != null) {
            UserCredential userDetails = myUserDetailService.loadUserByUsername(username);

            try {
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    return userDetails.getRole();
                }
            } catch (Exception e) {
                return "NONE";
            }
        }

        return "NONE";
    }


    /**
     * Authenticates the username-password combination it's handed in the form
     * of a request. If invalid, a BadCredentialException is thrown, informing
     * the user his/her credentials are invalid. If valid a JWT token is
     * generated which will allow for faster authentication for future requests.
     *
     * @param authenticationRequest request containing the user credentials
     * @return the generated JWT token
     */
    public String authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password.");
        }
        final UserDetails userDetails = myUserDetailService.loadUserByUsername(authenticationRequest
                                                            .getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * Activates the 'admin' account, which is the first admin account. This account
     * is required to create other admin accounts. It checks for validity of a secret
     * hard-coded activation token (which is handed upon buying/receiving the software).
     * If this token is valid it'll create the 'admin' account with the password in the
     * request.
     *
     * @param activationRequest request containing the activation token and passwords
     */
    public void activation(ActivationRequest activationRequest) {
        if (!activationRequest.getActivationKey().equals(activationKey)) {
            throw new InvalidParameterException("Invalid activation key.");
        }
        try {
            myUserDetailService.loadUserByUsername("admin");
            throw new InvalidParameterException("The activation key has already been used.");
        } catch (UsernameNotFoundException e) {
            if (!activationRequest.getPassword().equals(activationRequest.getPassword2())) {
                throw new InvalidParameterException("Passwords are not equal.");
            }
            final UserCredential userCredential = new UserCredential(
                    "admin",
                    encoder.encode(activationRequest.getPassword()),
                    adminRole
            );
            myUserDetailService.addUserCredential(userCredential);
        }
    }



    /**
     * Registers a new user. If the input is invalid, an InvalidParameterException
     * is thrown. If everything is fine, a user is created and added to the
     * `Authentication` database. After that, a request is sent to the `Users`
     * database. There, a user will be created which is then added to the `Users`
     * database.
     *
     * @param registrationRequest request containing information
     */
    public void registerUser(RegistrationRequest registrationRequest) {
        try {
            myUserDetailService.loadUserByUsername(registrationRequest.getUsername());
            throw new InvalidParameterException(
                    "The username '" + registrationRequest.getUsername() + "' is already in use."
            );
        } catch (UsernameNotFoundException e) {
            if (!registrationRequest.getUsername().contains("@")) {
                throw new InvalidParameterException("The username has to be an email.");
            }
            if (!registrationRequest.getPassword().equals(registrationRequest.getPassword2())) {
                throw new InvalidParameterException("Passwords are not equal.");
            }
            final UserCredential userCredential = new UserCredential(
                    registrationRequest.getUsername(),
                    encoder.encode(registrationRequest.getPassword()),
                    "USER"
            );
            myUserDetailService.addUserCredential(userCredential);

            UUID userUuid = userCredential.getUuid();
            String name = registrationRequest.getName();
            usersClient.addUser(userUuid, name);
        }
    }




}
