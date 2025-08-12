package com.example.vuespringexample;

import com.example.vuespringexample.jwt.JWTAuthenticationFilter;
import com.example.vuespringexample.jwt.JWTAuthorizationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    protected CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        LOGGER.debug("protected void configure(HttpSecurity http) throws Exception {");

        http
            .cors(Customizer.withDefaults())
            // トークン認証ではCSRF対策を用いない
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests((auth) -> auth
                                   .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                                   .permitAll()
                                   .requestMatchers(new AntPathRequestMatcher("/static/**"))
                                   .permitAll()
                                   .requestMatchers(new AntPathRequestMatcher("/"))
                                   .permitAll()
                                   .requestMatchers(new AntPathRequestMatcher("/login"))
                                   .permitAll()
                                   .requestMatchers(new AntPathRequestMatcher("/api/greeting"))
                                   .permitAll()
                                   .anyRequest()
                                   .authenticated()
            )

            // /logout
            .logout((logout) -> logout
                    .permitAll()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            )
            .addFilterBefore(corsFilter(), WebAsyncManagerIntegrationFilter.class)
            .addFilter(new JWTAuthenticationFilter(authenticationManager(userDetailsService(), PasswordEncoderFactories.createDelegatingPasswordEncoder())))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(userDetailsService(), PasswordEncoderFactories.createDelegatingPasswordEncoder())))

            // this disables session creation on Spring Security
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
