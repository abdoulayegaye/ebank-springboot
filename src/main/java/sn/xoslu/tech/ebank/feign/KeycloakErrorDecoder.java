package sn.xoslu.tech.ebank.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KeycloakErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = new String(response.body().asInputStream().readAllBytes());
            log.error("Keycloak error — status: {}, body: {}", response.status(), body);  // ← log l'erreur exacte

            return switch (response.status()) {
                case 400 -> new BadCredentialsException("Requête invalide : " + body);
                case 401 -> new BadCredentialsException("Identifiants invalides");
                case 403 -> new BadCredentialsException("Accès refusé");
                default -> new RuntimeException("Erreur Keycloak : " + body);
            };
        } catch (Exception e) {
            return new RuntimeException("Erreur lecture réponse Keycloak");
        }
    }
}
