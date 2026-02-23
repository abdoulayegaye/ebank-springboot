package sn.xoslu.tech.ebank.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.xoslu.tech.ebank.annotations.UniqueEmail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    @NotBlank(message = "Le nom est obligatoire.")
    private String name;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "Le format de l'email est invalide.")
    @UniqueEmail
    private String email;
}
