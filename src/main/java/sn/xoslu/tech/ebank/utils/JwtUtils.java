package sn.xoslu.tech.ebank.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//@Component
public class JwtUtils {

    // ✅ Récupère le token JWT courant
    private Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Aucun token JWT trouvé");
        }
        return jwt;
    }

    // ✅ Username
    public String getUsername() {
        return getCurrentJwt().getClaim("preferred_username"); // → "admin"
    }

    // ✅ Rôles depuis resource_access.ebank-client
    public List<String> getRoles() {
        Jwt jwt = getCurrentJwt();
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return List.of();

        Map<String, Object> ebankClient = (Map<String, Object>) resourceAccess.get("ebank-client");
        if (ebankClient == null) return List.of();

        List<String> roles = (List<String>) ebankClient.get("roles");
        return roles != null ? roles : List.of(); // → ["ADMIN"]
    }

    // ✅ Vérifie si l'utilisateur a un rôle spécifique
    public boolean hasRole(String role) {
        return getRoles().contains(role); // → hasRole("ADMIN")
    }

    // ✅ Autres claims utiles
    public String getEmail() {
        return getCurrentJwt().getClaim("email");
    }

    public String getFullName() {
        return getCurrentJwt().getClaim("name");
    }

    public String getUserId() {
        return getCurrentJwt().getClaim("sub"); // UUID Keycloak
    }
}
