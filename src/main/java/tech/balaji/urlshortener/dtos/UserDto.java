package tech.balaji.urlshortener.dtos;

import lombok.Data;
import tech.balaji.urlshortener.models.User;

@Data
public class UserDto {

    private Long id;
    private String userName;
    private String fullName;
    private String photoUrl;

    public static UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setPhotoUrl(user.getPhotoUrl());
        userDto.setUserName(user.getUsername());
        return userDto;
    }


}
