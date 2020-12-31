package tech.balaji.urlshortener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.balaji.urlshortener.dtos.AuthRequest;
import tech.balaji.urlshortener.dtos.CreateUserRequest;
import tech.balaji.urlshortener.dtos.UserDto;
import tech.balaji.urlshortener.models.User;
import tech.balaji.urlshortener.response.ApiSuccessResponse;
import tech.balaji.urlshortener.services.UserService;
import tech.balaji.urlshortener.utils.JwtTokenUtil;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static tech.balaji.urlshortener.utils.MessageConstants.*;

@Tag(name = "Authentication")
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserAuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/u/sign-up")
    public ResponseEntity<ApiSuccessResponse<UserDto>> signUp(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDto signedUpUser = userService.createUser(createUserRequest);
        return status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(signedUpUser, SIGNED_UP));
    }

    @PostMapping("/u/login")
    public ResponseEntity<ApiSuccessResponse<UserDto>> login(@RequestBody @Valid AuthRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            User user = (User) authenticate.getPrincipal();
            return ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
                    .body(new ApiSuccessResponse<>(UserDto.getUserDto(user), LOGGED_IN));
        } catch (BadCredentialsException ex) {
            return status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/u/logout")
    public ResponseEntity<ApiSuccessResponse<UserDto>> logout(@RequestBody @Valid AuthRequest authRequest) {
        UserDto signedUpUser = userService.logout(authRequest);
        return ok(new ApiSuccessResponse<>(signedUpUser, LOGGED_OUT));
    }

}
