package tech.balaji.urlshortener.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiSuccessResponse<T> {

    private String message;
    private T data;

    public ApiSuccessResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }


}
