package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable())

            .httpBasic((httpBasic) -> httpBasic.disable())

            .cors(Customizer.withDefaults())
            .authorizeHttpRequests((authz) -> authz
                                   .requestMatchers(new AntPathRequestMatcher("/"), new AntPathRequestMatcher("/home"))
                                   .permitAll()
                                   .anyRequest()
                                   .authenticated()
            )

            .formLogin((form) -> form.loginPage("/login")
                       .permitAll()
            )

            .logout((logout) -> logout.permitAll())
            ;

        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user =
            User.builder()
            .username("user")
            .password("password")
            .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

}
