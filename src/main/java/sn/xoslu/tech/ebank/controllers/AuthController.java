package sn.xoslu.tech.ebank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.AuthRequest;
import sn.xoslu.tech.ebank.feign.KeycloakAuthService;
import sn.xoslu.tech.ebank.feign.KeycloakTokenResponse;

@RestController
@RequestMapping("/authenticate")
@Tag(name = "Authentification", description = "Gestion de l'authentification")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;

    @GetMapping("/infos")
    @ResponseBody
    @Operation(hidden = true)
    public Authentication authentication(Authentication authentication){
        return authentication;
    }

    @PostMapping("/keycloak")
    @Operation(summary = "Authentification", description = "Authentification")
    public KeycloakTokenResponse authenticateByKeycloak(@RequestBody AuthRequest authRequest) {
        log.info("Authenticating with Keycloak");
        KeycloakTokenResponse response = null;
        try {
            response = keycloakAuthService.authenticate(authRequest.getUsername(), authRequest.getPassword());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Token Generated {}", response.getAccessToken());
        return response;
    }
}
