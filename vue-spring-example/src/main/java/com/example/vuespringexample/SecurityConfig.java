package com.example.vuespringexample;

import com.example.vuespringexample.jwt.JWTAuthenticationFilter;
import com.example.vuespringexample.jwt.JWTAuthorizationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    protected CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.debug("protected void configure(HttpSecurity http) throws Exception {");

        http
            .cors()
            // トークン認証ではCSRF対策を用いない
            .and()
            .csrf()
            .disable()

            .authorizeRequests()
            .antMatchers("/favicon.ico")
            .permitAll()
            .antMatchers("/static/**")
            .permitAll()
            .antMatchers("/")
            .permitAll()
            .antMatchers("/login")
            .permitAll()
            .antMatchers("/api/greeting")
            .permitAll()
            .anyRequest()
            .authenticated()

            // /logout
            .and()
            .logout()
            .permitAll()
            .invalidateHttpSession(true).deleteCookies("JSESSIONID")

            .and()
            .addFilterBefore(corsFilter(), WebAsyncManagerIntegrationFilter.class)
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))

            // this disables session creation on Spring Security
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("username")
            .password("{noop}password")
            .roles("USER")
            ;
    }

    // @Bean
    // protected CorsConfigurationSource corsConfigurationSource() {
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    //     source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

    //     return source;
    // }
}
