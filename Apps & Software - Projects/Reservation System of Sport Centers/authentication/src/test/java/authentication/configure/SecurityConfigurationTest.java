package authentication.configure;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityConfigurationTest {

    private static SecurityConfiguration securityConfiguration;

    @MockBean
    AuthenticationManagerBuilder auth;

    @BeforeAll
    public static void init() {
        securityConfiguration = new SecurityConfiguration();
    }

    @Test
    public void passwordEncoder_UsesBCEncoder() {
        BCryptPasswordEncoder expected = new BCryptPasswordEncoder();
        PasswordEncoder pwEncoder = securityConfiguration.passwordEncoder();
        assertEquals("passwordEncoder should use BCryptPasswordEncoder",
                expected.getClass(), pwEncoder.getClass());
    }

    //The rest I cannot seem to test as spring doesn't allow me too
}
