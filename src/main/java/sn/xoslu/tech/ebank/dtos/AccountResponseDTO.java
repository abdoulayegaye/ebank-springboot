package sn.xoslu.tech.ebank.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String numero;
    private Instant createdAt;
    private double balance;
    private boolean active;
    private CustomerDTO owner;
}
