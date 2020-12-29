package tech.balaji.urlshortener.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class AuthRequest {

    @NotNull
    @Email
    private String userName;
    @NotNull
    private String password;

}
