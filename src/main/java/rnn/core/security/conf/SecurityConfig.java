package rnn.core.security.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import rnn.core.security.authentication.CustomOAuth2SuccessHandler;
import rnn.core.security.model.Role;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebMvcConfigurationSupport {
    @Value("${frontend.url}")
    private String frontendUr;

    @Value("${frontend.auth_url}")
    private String frontendAuthUrl;

    private final CustomOAuth2SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(req -> req
                        .requestMatchers("/admin/**").hasAuthority(Role.Name.ADMIN.name())
                        .requestMatchers("/user/**").hasAnyAuthority(Role.Name.ADMIN.name(), Role.Name.USER.name())
                        .anyRequest().authenticated()
                )
                .oauth2Login(l -> l
                        .successHandler(successHandler)
                )
                .logout(l -> l
                        .logoutSuccessUrl(frontendAuthUrl)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(this.corsConfigurationSource()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUr));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
