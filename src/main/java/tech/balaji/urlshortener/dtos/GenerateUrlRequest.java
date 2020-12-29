package tech.balaji.urlshortener.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateUrlRequest {

    private String url;
    private String title;

}
