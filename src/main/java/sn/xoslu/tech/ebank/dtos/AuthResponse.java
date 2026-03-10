package sn.xoslu.tech.ebank.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private UserDTO user;
    private String roles;
}
