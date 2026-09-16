package RepoMind.backend.config;

import RepoMind.backend.security.GithubOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final GithubOAuth2UserService githubOAuth2UserService;
  private final AuthenticationSuccessHandler oauth2SuccessHandler;
  private final AuthenticationFailureHandler oauth2FailureHandler;

  @Bean
  SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/api/auth/login-url", "/oauth2/**", "/login/oauth2/**", "/error")
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers("/api/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .oauth2Login(
            oauth ->
                oauth
                    .userInfoEndpoint(userInfo -> userInfo.userService(githubOauth2UserService))
                    .successHandler(oauth2SuccessHandler)
                    .failureHandler(oauth2FailureHandler))
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessHandler(
                        ((request, response, authentication) ->
                            response.setStatus(HttpStatus.NO_CONTENT.value())))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("REPOMIND_SESSION"));

    return http.build();
  }

  @Bean
  AuthenticationSuccessHandler oauth2SuccessHandler(
      @Value("${app.frontend-url}") String frontendUrl) {
    SimpleUrlAuthenticationSuccessHandler successHandler =
        new SimpleUrlAuthenticationSuccessHandler();
    successHandler.setDefaultTargetUrl(frontendUrl + "/auth/callback");
    return successHandler;
  }

  @Bean
  AuthenticationFailureHandler oauth2FailureHandler(
      @Value("${app.frontend-url}") String frontendUrl) {
    SimpleUrlAuthenticationFailureHandler failureHandler =
        new SimpleUrlAuthenticationFailureHandler();
    failureHandler.setDefaultFailureUrl(frontendUrl + "/login?error=oauth_failed");
    return failureHandler;
  }
}
