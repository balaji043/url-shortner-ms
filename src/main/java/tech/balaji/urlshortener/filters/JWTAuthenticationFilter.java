package tech.balaji.urlshortener.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.balaji.urlshortener.dtos.AuthData;
import tech.balaji.urlshortener.dtos.AuthRequest;
import tech.balaji.urlshortener.dtos.UserDto;
import tech.balaji.urlshortener.models.User;
import tech.balaji.urlshortener.properties.ApplicationProperties;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static tech.balaji.urlshortener.utils.MessageConstants.LOGIN_URL;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ApplicationProperties applicationProperties;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationProperties applicationProperties) {
        this.authenticationManager = authenticationManager;
        this.applicationProperties = applicationProperties;

        setFilterProcessesUrl(LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) {
        try {
            AuthRequest creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AuthRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserName(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + applicationProperties.getExpirationTime()))
                .sign(Algorithm.HMAC512(applicationProperties.getJwtSecret().getBytes()));

        User user = (User) auth.getPrincipal();

        AuthData authData = new AuthData();
        authData.setToken(token);
        authData.setUserDetails(UserDto.getUserDto(user));
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(new ObjectMapper().writeValueAsString(authData));
        res.getWriter().flush();
    }
}
