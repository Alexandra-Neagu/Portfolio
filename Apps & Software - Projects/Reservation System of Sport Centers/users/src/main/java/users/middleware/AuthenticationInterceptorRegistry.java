package users.middleware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The class AuthenticationInterceptorRegistry.
 */
@Configuration
public class AuthenticationInterceptorRegistry implements WebMvcConfigurer {

    /**
     * Authentication interceptor authentication interceptor.
     *
     * @return the authentication interceptor
     */
    @Bean
    AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor());
    }
}
