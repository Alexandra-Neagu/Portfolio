package authentication.services;

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
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * User details service. Operates as temporary database.
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UsersClient usersClient;



    /**
     * Get the user from the database with a username.
     *
     * @param username Username of user.
     * @return User details of the user matching the username.
     * @throws UsernameNotFoundException Exception thrown if username is not found.
     */
    @Override
    public UserCredential loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential user = userCredentialRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    /**
     * Adds a new user credential to the database.
     *
     * @param userCredential the user credentials
     */
    public UserCredential addUserCredential(UserCredential userCredential) {
        return userCredentialRepository.save(userCredential);
    }

    /**
     * Removes a user credential from the database.
     *
     * @param userCredential the user credentials
     */
    public void deleteUserCredential(UserCredential userCredential) {
        userCredentialRepository.delete(userCredential);
    }


}
