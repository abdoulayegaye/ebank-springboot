package sn.xoslu.tech.ebank.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.xoslu.tech.ebank.entities.Account;
import sn.xoslu.tech.ebank.enums.TypeOperation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationDTO {
    private Long id;
    private String numero;
    private TypeOperation type;
    private double amount;
    private Account account;
}
