package authentication.api;

import authentication.models.ActivationRequest;
import authentication.models.AdminRequest;
import authentication.models.AuthenticationRequest;
import authentication.models.AuthenticationResponse;
import authentication.models.DeletionRequest;
import authentication.models.PasswordRequest;
import authentication.models.RegistrationRequest;
import authentication.services.AuthenticateService;
import authentication.services.FilteredAuthenticationService;
import authentication.services.MyUserDetailService;
import authentication.util.JwtUtil;
import java.security.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication API containing the endpoints and calling MyUserDetailService
 * for functionality to form the desired action/response to each request.
 */
@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationAPI {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private FilteredAuthenticationService filteredAuthenticationService;

    /**
     * Mapping to /permit.
     *
     * @return the premission
     */
    @GetMapping("/permit")
    public ResponseEntity<String> permit(@RequestParam("jwt") String jwt) {
        String permissions = authenticateService.getPermissions(jwt, jwtTokenUtil);
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    /**
     * Endpoint called for authentication of user credentials.
     * Calls 'authenticate' method in the userDetailService, which
     * either returns a key or throws an exception if the input is
     * invalid.
     *
     * @param authenticationRequest request containing user credentials
     * @return ResponseEntity containing valid JWT token
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        if (
                authenticationRequest.getUsername() == null
                || authenticationRequest.getPassword() == null
        ) {
            throw new InvalidParameterException("Invalid input: username and password required.");
        }
        final String jwt = authenticateService.authenticate(authenticationRequest);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    /**
     * Endpoint called to activate the first admin account. Calls the
     * 'activation' method in the userDetailService. Which throws an exception
     * if the input is invalid.
     *
     * @param activationRequest request containing activation key and passwords
     * @return ResponseEntity informing user of success
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public ResponseEntity<String> activation(
            @RequestBody ActivationRequest activationRequest
    ) {
        if (
                activationRequest.getActivationKey() == null
                || activationRequest.getPassword() == null
                || activationRequest.getPassword2() == null
        ) {
            throw new InvalidParameterException(
                    "Invalid input: activationKey, password and password2 required."
            );
        }

        authenticateService.activation(activationRequest);
        return new ResponseEntity<>(
                "Activation successful!\nUse the username 'admin' "
                + "with the password used in activation, to log in.",
                HttpStatus.OK
        );
    }

    /**
     * Endpoint called to create an admin account. Calls the 'addAdmin'
     * method in the userDetailService. Which trows an exception if the
     * input is invalid.
     *
     * @param authorization Authorization header holding the JWT token
     * @param adminRequest request containing required information to
     *                     create the admin account
     * @return ResponseEntity informing user of success
     * @throws IllegalAccessException exception thrown if a non-admin
     *      user sends a request to this endpoint
     */
    @RequestMapping(value = "/add-admin", method = RequestMethod.POST)
    public ResponseEntity<String> adminRegistration(
            @RequestHeader("Authorization") String authorization,
            @RequestBody AdminRequest adminRequest
    ) throws IllegalAccessException {
        if (
                adminRequest.getUsername() == null
                || adminRequest.getPassword() == null
                || adminRequest.getPassword2() == null
        ) {
            throw new InvalidParameterException(
                    "Invalid input: username, password and password2 required."
            );
        }

        filteredAuthenticationService.addAdmin(authorization, adminRequest);
        return new ResponseEntity<>("The admin account has been created.", HttpStatus.OK);
    }

    /**
     * Endpoint called to register a new user. Calls the 'registerUser'
     * method in the userDetailService. Which throws an exception if the
     * input is invalid.
     *
     * @param registrationRequest request containing required information to
     *                            create a new user
     * @return ResponseEntity informing user of success
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> userRegistration(
            @RequestBody RegistrationRequest registrationRequest
    ) {
        if (
                registrationRequest.getName() == null
                || registrationRequest.getUsername() == null
                || registrationRequest.getPassword() == null
                || registrationRequest.getPassword2() == null
        ) {
            throw new InvalidParameterException(
                    "Invalid input: name, username, password and password2 required."
            );
        }

        authenticateService.registerUser(registrationRequest);
        return new ResponseEntity<>("Your account has been created.", HttpStatus.OK);
    }

    /**
     * Endpoint called to change the password of the user. Calls the
     * 'changePasswordUser' method in the userDetailService. Which
     * throws an exception if the input is invalid.
     *
     * @param authorization Authorization header holding the JWT token
     * @param passwordRequest request containing old and new password
     * @return ResponseEntity informing user of success
     */
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody PasswordRequest passwordRequest
    ) {
        if (
                passwordRequest.getPassword() == null
                || passwordRequest.getNewPassword() == null
                || passwordRequest.getNewPassword2() == null
        ) {
            throw new InvalidParameterException(
                    "Invalid input: password, newPassword and newPassword2 required."
            );
        }

        filteredAuthenticationService.changePasswordUser(authorization, passwordRequest);
        return new ResponseEntity<>("Your password has successfully changed.", HttpStatus.OK);
    }

    /**
     * Endpoint called to delete the account of the user. Calls the
     * 'deleteUser' method of the userDetailService. Which throws an
     * exception if the password is incorrect.
     *
     * @param authorization Authorization header holding the JWT token
     * @param deletionRequest request containing the password
     * @return ResponseEntity informing user of success
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> userDeletion(
            @RequestHeader("Authorization") String authorization,
            @RequestBody DeletionRequest deletionRequest
    ) {
        if (
                deletionRequest.getPassword() == null
        ) {
            throw new InvalidParameterException("Invalid input: password required.");
        }

        filteredAuthenticationService.deleteUser(authorization, deletionRequest);

        return new ResponseEntity<>("Your account has been deleted.", HttpStatus.OK);
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Object> badParameterExceptionHandler(
            InvalidParameterException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    /**
     * An exception handler for when the user credentials are not valid.
     *
     * @param exception exception that has occurred
     * @return response body containing the reason for the exception with a 400 status code
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Object> badCredentialsExceptionHandler(
            BadCredentialsException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    /**
     * An exception handler for when the user has no access to what he/she is trying to reach.
     *
     * @param exception exception that has occurred
     * @return response body containing the reason for the exception with a 400 status code
     */
    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Object> illegalAccessExceptionHandler(
            IllegalAccessException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
