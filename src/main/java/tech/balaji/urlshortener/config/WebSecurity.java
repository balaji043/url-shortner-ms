package tech.balaji.urlshortener.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech.balaji.urlshortener.filters.JWTAuthenticationFilter;
import tech.balaji.urlshortener.filters.JWTAuthorizationFilter;
import tech.balaji.urlshortener.properties.ApplicationProperties;
import tech.balaji.urlshortener.services.implementation.UserServiceImpl;

import static java.lang.String.format;
import static tech.balaji.urlshortener.utils.MessageConstants.*;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties applicationProperties;
    private final UserServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(format("%s/**", applicationProperties.getRestApiDocPath()))
                .permitAll()
                .antMatchers(format("%s/**", applicationProperties.getSwaggerPath()))
                .permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, LOGIN_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, REDIRECT_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, "/a/*")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), applicationProperties
                ))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), applicationProperties))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
