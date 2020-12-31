package tech.balaji.urlshortener.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app")
@Data
@Configuration
@Validated
public class ApplicationProperties {

    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String version;

    @NotNull
    private String jwtSecret;
    @NotNull
    private String jwtIssuer;
    @NotNull
    private long expirationTime;

    @NotNull
    private String restApiDocPath;
    @NotNull
    private String swaggerPath;
    @NotNull
    private String notFoundUrl;

}
