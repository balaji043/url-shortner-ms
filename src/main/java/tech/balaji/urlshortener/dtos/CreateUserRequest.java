package tech.balaji.urlshortener.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserRequest {
    @NotNull
    @NotBlank
    @Email
    private String userName;
    @NotBlank
    private String fullName;
    @NotBlank
    private String password;
    private String photoUrl;
}
