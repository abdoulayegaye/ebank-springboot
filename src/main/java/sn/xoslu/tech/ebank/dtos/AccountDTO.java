package sn.xoslu.tech.ebank.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.xoslu.tech.ebank.entities.Customer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private String numero;
    private double balance;
    private boolean active = true;
    private Customer customer;
}
