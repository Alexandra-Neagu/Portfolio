package authentication.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The UserCredential entity.
 */
@Entity
@Table(name = "user_credential")
public class UserCredential implements UserDetails {

    public static final long serialVersionUID = 1234323143;

    /**
     * The UUID of the UserCredential in order to uniquely identify the object.
     */
    @Id
    @Column(name = "uuid")
    private final UUID uuid;

    /**
     * The username of the object.
     */
    @Column(name = "username")
    private String username;

    /**
     * The password of the object.
     */
    @Column(name = "password")
    private String password;

    /**
     * The role of the object.
     */
    @Column(name = "role")
    private String role;

    /**
     * Empty constructor.
     */
    public UserCredential() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructor.
     *
     * @param username the given username
     * @param password the given password
     */
    public UserCredential(String username, String password, String role) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        assert password != null;
        this.password = password;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Checks if two UserCredential objects are equal.
     *
     * @param o Object
     * @return boolean depending on their equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCredential other = (UserCredential) o;
        return this.uuid.equals(other.uuid);
    }

    /**
     * Returns the String representation of the object.
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return "User{"
                + "uuid=" + uuid
                + ", username='" + username + '\''
                + ", password='" + password + '\''
                + ", role='" + role + '\''
                + '}';
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, password, role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
