package tech.balaji.urlshortener.dtos;

import lombok.Data;

@Data
public class AuthData {
    private UserDto userDetails;
    private String token;
}
