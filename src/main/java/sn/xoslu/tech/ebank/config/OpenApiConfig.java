package sn.xoslu.tech.ebank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 🔹 Version OpenAPI (important pour Swagger UI)
                .openapi("3.0.1")

                // 🔹 Informations générales API
                .info(new Info()
                        .title("API Banking")
                        .version("1.0.0")
                        .description("""
                                Documentation de l'API Banking.

                                Cette API permet de gérer :
                                - Les clients
                                - Les comptes bancaires
                                - Les opérations (dépôt, retrait, virement)

                                Projet réalisé avec les outils et technos suivants :
                                - Java
                                - Spring Boot
                                - Spring Security / JWT / OAuth2 / OIDC / Keycloak
                                - Spring Data JPA
                                - OpenAPI / Swagger
                                - Git
                                - GitLab
                                - Docker
                                """)
                        .termsOfService("https://banking.com/terms")

                        // 🔹 Contact
                        .contact(new Contact()
                                .name("Equipe Banking IT")
                                .email("support@banking.com")
                                .url("https://banking.com"))

                        // 🔹 Licence
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                )

                // 🔹 Serveurs disponibles
                .servers(List.of(

                        new Server()
                                .url("http://localhost:8080/api/v1")
                                .description("Local DEV Server"),

                        new Server()
                                .url("http://localhost:8088/api/v1")
                                .description("Docker Local"),

                        new Server()
                                .url("https://api.banking.com")
                                .description("Production Server")

                ))

                // 🔹 Sécurité JWT / Bearer
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",

                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Authentification JWT Bearer Token")
                        )
                )

                // 🔹 Security globale appliquée à toutes les APIs
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"));
    }
}

