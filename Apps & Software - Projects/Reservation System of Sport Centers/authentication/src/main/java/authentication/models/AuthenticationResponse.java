package authentication.models;

import java.util.Objects;

/**
 * Authentication response.
 */
public class AuthenticationResponse {

    private final String jwt;

    /**
     * Constructor of the response.
     *
     * @param jwt jwt token
     */
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Getter of the jwt.
     *
     * @return the jwt String
     */
    public String getJwt() {
        return jwt;
    }

    /**
     *  Hash function.
     *
     * @return the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }

    /**
     * Checks if two AuthenticationResponse are equal.
     *
     * @param o the AuthenticationResponse to compare to.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticationResponse)) {
            return false;
        }
        AuthenticationResponse that = (AuthenticationResponse) o;
        return Objects.equals(jwt, that.jwt);
    }

}
