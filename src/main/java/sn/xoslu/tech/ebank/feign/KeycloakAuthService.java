package sn.xoslu.tech.ebank.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sn.xoslu.tech.ebank.dtos.AuthResponse;
import sn.xoslu.tech.ebank.dtos.UserDTO;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthService {

    private final KeycloakFeignClient keycloakFeignClient;
    private final KeycloakProperties keycloakProperties;

    public KeycloakTokenResponse authenticate(String username, String password) throws JsonProcessingException {

        // 1. Récupère le token depuis Keycloak
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("grant_type", "password");
        formParams.add("client_id", keycloakProperties.getClientId());
        formParams.add("client_secret", keycloakProperties.getClientSecret());
        formParams.add("username", username);
        formParams.add("password", password);

        KeycloakTokenResponse tokenResponse = keycloakFeignClient.getToken(formParams);

        // 2. Décode le token JWT sans vérification (déjà validé par Keycloak)
        String accessToken = tokenResponse.getAccessToken();
        Map<String, Object> claims = decodeClaims(accessToken);

        // 3. Extrait username et rôles
        String extractedUsername = (String) claims.get("preferred_username");
        List<String> roles = extractRoles(claims);

        UserDTO userDTO = new UserDTO();
        userDTO.setId((String) claims.get("sub"));
        userDTO.setUsername(extractedUsername);
        userDTO.setFirstname((String) claims.get("given_name"));
        userDTO.setLastname((String) claims.get("family_name"));
        userDTO.setEmail((String) claims.get("email"));
        userDTO.setPhone("771800510");

        // 4. Retourne tout
        return KeycloakTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(tokenResponse.getRefreshToken())
                .username(extractedUsername)
                .roles(roles)
                .tokenType("Bearer")
                .user(userDTO)
                .build();
    }

    // Décode le payload Base64 du JWT
    private Map<String, Object> decodeClaims(String token) throws JsonProcessingException {
        String payload = token.split("\\.")[1]; // header.PAYLOAD.signature
        byte[] decoded = Base64.getUrlDecoder().decode(payload);
        String json = new String(decoded, StandardCharsets.UTF_8);

        return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
    }

    // Extrait les rôles depuis resource_access.ebank-client.roles
    private List<String> extractRoles(Map<String, Object> claims) {
        try {
            Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
            Map<String, Object> ebankClient = (Map<String, Object>) resourceAccess.get(keycloakProperties.getClientId());
            return (List<String>) ebankClient.get("roles"); // → ["ADMIN"]
        } catch (Exception e) {
            return List.of();
        }
    }
}
