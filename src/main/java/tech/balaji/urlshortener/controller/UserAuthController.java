package tech.balaji.urlshortener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.balaji.urlshortener.dtos.CreateUserRequest;
import tech.balaji.urlshortener.dtos.UserDto;
import tech.balaji.urlshortener.response.ApiSuccessResponse;
import tech.balaji.urlshortener.services.UserService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.status;
import static tech.balaji.urlshortener.utils.MessageConstants.SIGNED_UP;
import static tech.balaji.urlshortener.utils.MessageConstants.SIGN_UP_URL;

@Tag(name = "Authentication")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<ApiSuccessResponse<UserDto>> signUp(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDto signedUpUser = userService.createUser(createUserRequest);
        return status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(signedUpUser, SIGNED_UP));
    }

}
