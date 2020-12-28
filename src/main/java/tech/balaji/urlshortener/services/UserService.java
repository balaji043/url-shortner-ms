package tech.balaji.urlshortener.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import tech.balaji.urlshortener.dtos.AuthRequest;
import tech.balaji.urlshortener.dtos.CreateUserRequest;
import tech.balaji.urlshortener.dtos.UserDto;
import tech.balaji.urlshortener.models.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    UserDto createUser(CreateUserRequest createUserRequest);

    UserDto login(AuthRequest authRequest);

    UserDto logout(AuthRequest authRequest);

    Optional<User> getUser();
}
