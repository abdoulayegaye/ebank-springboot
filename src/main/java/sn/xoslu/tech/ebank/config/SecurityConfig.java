package sn.xoslu.tech.ebank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sn.xoslu.tech.ebank.filters.JwtAuthFilter;
import sn.xoslu.tech.ebank.services.impl.UserInfoUserDetailsService;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthFilter authFilter;

    private static final String[] AUTHORIZED_WHITELIST = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/authenticate"
    };

    /*
    * Sans lui, Spring Security utilise par défaut NoOpPasswordEncoder (mot de passe en clair),
    * ce qui est dangereux en production. Dès que tu stockes des mots de passe, il te le faut.
    * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    * Obligatoire si tu veux que Spring Security charge tes utilisateurs depuis ta propre source (BDD, etc.)
    * */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                /*
                * Configure le Cross-Origin Resource Sharing. Cela bloque les requêtes venant d'un autre domaine
                * (ex: ton frontend React sur localhost:3000 qui appelle ton API sur localhost:8080).
                * Sans ça, le navigateur bloque les requêtes cross-origin.
                * */
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                /*
                * Désactive la protection CSRF (Cross-Site Request Forgery).
                * C'est normal et correct dans une API REST avec JWT,
                * car le CSRF ne concerne que les sessions avec cookies.
                * Avec des tokens Bearer dans les headers, tu n'as pas ce risque.
                * */
                .csrf(csrf -> csrf.disable())
                /*
                * Définit qui peut accéder à quoi
                * */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTHORIZED_WHITELIST).permitAll() // Ces routes sont publiques (login, register, etc.)
                        .anyRequest().authenticated() // Tout le reste nécessite d'être connecté
                )
                /*
                * Indique à Spring Security de ne pas créer de session HTTP.
                * Chaque requête doit s'authentifier par elle-même via le token JWT dans le header.
                 * */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                /*
                * Branche ton `AuthenticationProvider` (le `DaoAuthenticationProvider` configuré
                * avec ton `UserDetailsService` et ton `PasswordEncoder`) pour que Spring sache comment
                * vérifier les credentials.
                * */
                .authenticationProvider(authenticationProvider())
                /*
                * Insère ton filtre JWT (`authFilter`) dans la chaîne,
                * avant le filtre d'authentification par défaut de Spring.
                *
                * Concrètement, à chaque requête ton `authFilter` va :
                * 1. Extraire le token JWT du header `Authorization`
                * 2. Le valider
                * 3. Charger l'utilisateur et mettre son authentification dans le `SecurityContext`
                * */
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /*
    * Tu en as besoin seulement si tu gères toi-même l'authentification dans un contrôleur ou un filtre
    * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
    * ## 🔄 Vue d'ensemble du flux
        Requête HTTP entrante
            ↓
       Filtre CORS        → autorise/bloque selon l'origine
            ↓
       authFilter (JWT)   → valide le token, authentifie l'utilisateur
            ↓
       Authorization      → whitelist ? → OK | sinon → authentifié ?
            ↓
       Controller         → traitement de la requête
    * */
}
