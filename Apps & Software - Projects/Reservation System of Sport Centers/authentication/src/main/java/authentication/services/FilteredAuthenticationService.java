package authentication.services;

import authentication.clients.UsersClient;
import authentication.entities.UserCredential;
import authentication.models.AdminRequest;
import authentication.models.DeletionRequest;
import authentication.models.PasswordRequest;
import authentication.repositories.UserCredentialRepository;
import authentication.util.JwtUtil;
import java.security.InvalidParameterException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FilteredAuthenticationService {

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

    final PasswordEncoder encoder = new BCryptPasswordEncoder();
    final String adminRole = "ADMIN";

    /**
     * Deletes the account of the user. If the password in the request does
     * not match the password of the user, an InvalidParameterException is
     * thrown. If the passwords match, the deletion starts. First the UUID
     * of the user being deleted is retrieved and sent in a request to the
     * `Users` microservice where the user is also deleted. After that,
     * the user is also deleted in `Authentication`.
     *
     * @param authorization Authorization header holding the JWT token
     * @param deletionRequest request containing the password of the user
     */
    public void deleteUser(String authorization, DeletionRequest deletionRequest) {
        final String token = authorization.substring(7);
        final String username = jwtTokenUtil.extractUsername(token);

        // This checks if the username password combination checks out.
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username, deletionRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect password.");
        }

        UserCredential userCredential = myUserDetailService.loadUserByUsername(username);
        myUserDetailService.deleteUserCredential(userCredential);

        if (!userCredential.getRole().equals(adminRole)) {
            UUID userUuid = userCredential.getUuid();
            usersClient.deleteUser(userUuid);
        }
    }

    /**
     * Changes the password of the user. If the old password does not match
     * the current password, or if the new password and second entry of the
     * new password are not equal, an InvalidParameterException is thrown.
     * If not, the user is loaded and deleted from the `Authentication`
     * database, then the password of the user is changed and the new instance
     * of the user is added to the `Authentication` database.
     *
     * @param authorization Authorization header holding the JWT token
     * @param passwordRequest request containing the old and the new password
     */
    public void changePasswordUser(String authorization, PasswordRequest passwordRequest) {
        if (!passwordRequest.getNewPassword().equals(passwordRequest.getNewPassword2())) {
            throw new InvalidParameterException("New passwords are not equal.");
        }

        // This extracts just the token from the header, so excluding 'Bearer '.
        final String token = authorization.substring(7);
        final String username = jwtTokenUtil.extractUsername(token);
        UserCredential oldUserCredential = myUserDetailService.loadUserByUsername(username);
        UserCredential newUserCredential = myUserDetailService.loadUserByUsername(username);

        // This checks if the username password combination checks out.
        if (!authenticateCredentials(username, passwordRequest.getPassword())) {
            throw new BadCredentialsException("Incorrect password.");
        }

        // Update password in the UserCredential, remove old from database and add new.
        newUserCredential.setPassword(encoder.encode(passwordRequest.getNewPassword()));
        myUserDetailService.deleteUserCredential(oldUserCredential);
        myUserDetailService.addUserCredential(newUserCredential);

        // Validates that the password has been changed correctly. If not, it reverts changes.
        if (!authenticateCredentials(username, passwordRequest.getNewPassword())) {
            myUserDetailService.addUserCredential(newUserCredential);
            myUserDetailService.deleteUserCredential(oldUserCredential);
            throw new BadCredentialsException(
                    "The passwords have not been changed correctly. Changes have been reverted."
            );
        }
    }

    private boolean authenticateCredentials(String username, String password) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return false;
        }
        return true;
    }

    /**
     * Creates an admin account. Admin accounts can only be created by other admins,
     * therefore the method checks whether the request comes from an admin user. If
     * so, the admin account is created. If not, an IllegalAccessException is thrown.
     * The added admin account is then added to the `Authentication` database.
     *
     * @param authorization Authorization header holding the JWT token
     * @param adminRequest request containing the required information to create
     *                     an admin account
     * @throws IllegalAccessException exception thrown if the request is made by a
     *      non-admin user
     */
    public void addAdmin(String authorization, AdminRequest adminRequest)
            throws IllegalAccessException {
        final String token = authorization.substring(7);
        final String username = jwtTokenUtil.extractUsername(token);
        final UserCredential user = myUserDetailService.loadUserByUsername(username);
        if (!user.getRole().equals(adminRole)) {
            throw new IllegalAccessException("You don't have access to this functionality.");
        }
        try {
            myUserDetailService.loadUserByUsername(adminRequest.getUsername());
            throw new InvalidParameterException(
                    "The username '" + adminRequest.getUsername() + "' is already in use."
            );
        } catch (UsernameNotFoundException e) {
            if (!adminRequest.getUsername().contains("@")) {
                throw new InvalidParameterException("The username has to be an email.");
            }
            if (!adminRequest.getPassword().equals(adminRequest.getPassword2())) {
                throw new InvalidParameterException("Passwords are not equal.");
            }
            final UserCredential userCredential = new UserCredential(
                    adminRequest.getUsername(),
                    encoder.encode(adminRequest.getPassword()),
                    adminRole
            );
            myUserDetailService.addUserCredential(userCredential);
        }
    }
}
