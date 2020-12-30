package tech.balaji.urlshortener.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app")
@Data
@Configuration
public class ApplicationProperties {

    private String title;
    private String description;
    private String version;

    private String jwtSecret;
    private String jwtIssuer;

    private String restApiDocPath;
    private String swaggerPath;
    private String notFoundUrl;

}
