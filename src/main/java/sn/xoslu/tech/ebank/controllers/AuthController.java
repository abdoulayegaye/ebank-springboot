package sn.xoslu.tech.ebank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.AuthRequest;
import sn.xoslu.tech.ebank.dtos.AuthResponse;
import sn.xoslu.tech.ebank.exceptions.UnauthorizedException;
import sn.xoslu.tech.ebank.services.JwtService;

@RestController
@RequestMapping("/authenticate")
@Tag(name = "Authentification", description = "Gestion de l'authentification")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info("auth request : {}", authRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        log.info("authentication {}", authentication);
        if (authentication.isAuthenticated()) {
            AuthResponse response = jwtService.generateToken(authRequest.getUsername());
            log.info("Token Generated {}", response.getToken());
            return response;
        } else {
            throw new UnauthorizedException("invalid user request !");
        }
    }

    @GetMapping("/infos")
    @ResponseBody
    @Operation(hidden = true)
    public Authentication authentication(Authentication authentication){
        return authentication;
    }
}
