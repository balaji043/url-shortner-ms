package tech.balaji.urlshortener.services.implementation;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.balaji.urlshortener.dtos.AuthRequest;
import tech.balaji.urlshortener.dtos.CreateUserRequest;
import tech.balaji.urlshortener.dtos.UserDto;
import tech.balaji.urlshortener.exceptions.CommonException;
import tech.balaji.urlshortener.models.User;
import tech.balaji.urlshortener.repository.UserRepository;
import tech.balaji.urlshortener.services.UserService;
import tech.balaji.urlshortener.utils.MessageConstants;

import java.util.Optional;

import static java.lang.String.format;
import static tech.balaji.urlshortener.dtos.UserDto.getUserDto;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserRequest createUserRequest) {
        if (userRepository.findByUserName(createUserRequest.getUserName()).isPresent())
            throw new CommonException(MessageConstants.EMAIL_ID_EXISTS, HttpStatus.BAD_REQUEST);
        User user = new User();
        BeanUtils.copyProperties(createUserRequest, user);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user = userRepository.save(user);
        return getUserDto(user);
    }

    @Override
    public UserDto login(AuthRequest authRequest) {
        return null;
    }

    @Override
    public UserDto logout(AuthRequest authRequest) {
        return null;
    }

    @Override
    public Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserName(authentication.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(format("User with email - %s, not found", userName)));
    }
}
