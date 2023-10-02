package authentication.entities;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserCredentialTest {
    private String username = "username";
    private String password = "password";
    private String role = "USER";

    @Test
    public void emptyConstructor_SetsCorrectProperties() {
        UserCredential userCredential = new UserCredential();
        assertNotEquals("UUID of UserCredentials is not set", null, userCredential.getUuid());
        assertEquals("Name is set incorrectly", null, userCredential.getUsername());
        assertEquals("Password is set incorrectly", null, userCredential.getPassword());
        assertEquals("Max capacity is set incorrectly", null, userCredential.getRole());
    }

    @Test
    public void constructorWithoutRole_CorrectRole() {
        UserCredential userCredential = new UserCredential(username, password, role);
        assertEquals("Role should be USER", role, userCredential.getRole());
    }

    @Test
    public void getterSetter_SetCorrectValues_WhenEverythingIsValid() {
        UserCredential userCredential = new UserCredential(username, "password", role);
        assertNotEquals("UUID should be set", null, userCredential.getUuid());
        userCredential.setPassword(password);
        assertEquals("Password should be the same", password, userCredential.getPassword());
        assertEquals("Username should be the same", username, userCredential.getUsername());
        assertEquals("Role should be the same", role, userCredential.getRole());
    }

    @Test
    public void hashCode_HashesCorrect_WhenHashing() {
        UserCredential userCredential = new UserCredential(username, password, role);
        assertEquals("It doesn't hash correctly",
                Objects.hash(userCredential.getUuid(), username, password, role),
                userCredential.hashCode());
    }

    @Test
    public void getAuthorities_ReturnsUser_WhenCalled() {
        UserCredential userCredential = new UserCredential();
        Set<GrantedAuthority> authorities = userCredential.getAuthorities();
        assertEquals("Authorities should include USER",
                true, authorities.contains(new SimpleGrantedAuthority("USER")));
    }

    @Test
    public void expectTrue_WhenOveriddenBooleanFunctionIsCalled() {
        UserCredential userCredential = new UserCredential();
        assertEquals("isAccountNonExpired should return true",
                true, userCredential.isAccountNonExpired());
        assertEquals("isAccountNonLocked should return true",
                true, userCredential.isAccountNonLocked());
        assertEquals("isCredentialsNonExpired should return true",
                true, userCredential.isCredentialsNonExpired());
        assertEquals("isEnabled should return true",
                true, userCredential.isEnabled());
    }

    @Test
    public void toString_shouldBeAString() {
        UserCredential userCredential = new UserCredential();
        String result = userCredential.toString();
        assertNotEquals("String should not be null", null, result);
        assertNotEquals("String should not be empty", "", result);
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSameObject() {
        UserCredential userCredential = new UserCredential();
        assertEquals("Object should be equal with itself", true,
                userCredential.equals(userCredential));
    }

    @Test
    public void equals_ReturnsFalse_WhenItHasDifferentUuids() {
        UserCredential userCredential = new UserCredential();
        UserCredential other = new UserCredential();
        assertEquals("Object with different Uuid should not be equal",
                false, userCredential.equals(other));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithADifferentClass() {
        UserCredential userCredential = new UserCredential();
        Set other = new HashSet<Integer>();
        assertEquals("Object with different Uuid should not be equal",
                false, userCredential.equals(other));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithNull() {
        UserCredential userCredential = new UserCredential();
        UserCredential other = null;
        assertEquals("Object should not be equal to null",
                false, userCredential.equals(other));
    }

    @Test
    public void equals_ReturnsEquals_WhenUuidIsEqual()
            throws NoSuchFieldException, IllegalAccessException {
        UserCredential userCredential = new UserCredential();
        UserCredential other = new UserCredential();

        Field nameField = userCredential.getClass().getDeclaredField("uuid");
        nameField.setAccessible(true);

        UUID uuid = other.getUuid();
        nameField.set(userCredential, uuid);
        assertEquals("UserCredential should be equal when uuid is the same",
                true, userCredential.equals(other));
    }

}
