package sn.xoslu.tech.ebank.mappers;

import sn.xoslu.tech.ebank.dtos.AccountDTO;
import sn.xoslu.tech.ebank.dtos.AccountResponseDTO;
import sn.xoslu.tech.ebank.entities.Account;

import java.util.List;

public interface AccountMapper {
    AccountDTO toDTO(Account account);
    AccountResponseDTO toResponseDTO(Account account);
    Account toEntity(AccountDTO dto);
    List<AccountDTO> toDTOList(List<Account> accounts);
    List<AccountResponseDTO> toResponseDTOList(List<Account> accounts);
}
