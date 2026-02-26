package sn.xoslu.tech.ebank.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.jwt")
@Component
@Validated  // active la validation
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtProperties {
    @NotBlank
    private String secret;
    @Positive
    private long expiration;
}
