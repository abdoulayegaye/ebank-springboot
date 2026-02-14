package sn.xoslu.tech.ebank.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.xoslu.tech.ebank.dtos.AccountDTO;
import sn.xoslu.tech.ebank.dtos.AccountResponseDTO;
import sn.xoslu.tech.ebank.entities.Account;
import sn.xoslu.tech.ebank.mappers.AccountMapper;
import sn.xoslu.tech.ebank.mappers.CustomerMapper;
import sn.xoslu.tech.ebank.services.CustomerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;
    
    @Override
    public AccountDTO toDTO(Account account) {
        if (account == null) return null;

        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setNumero(account.getNumero());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setBalance(account.getBalance());
        dto.setActive(account.isActive());
        dto.setCustomerId(customerMapper.toDTO(account.getCustomer()).getId());

        return dto;
    }

    @Override
    public AccountResponseDTO toResponseDTO(Account account) {
        if (account == null) return null;

        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setNumero(account.getNumero());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setBalance(account.getBalance());
        dto.setActive(account.isActive());
        dto.setOwner(customerMapper.toDTO(account.getCustomer()));

        return dto;
    }

    @Override
    public Account toEntity(AccountDTO dto) {
        if (dto == null) return null;

        return Account.builder()
                .id(dto.getId())
                .numero(dto.getNumero())
                .balance(dto.getBalance())
                .active(dto.isActive())
                .customer(customerMapper.toEntity(customerService.getCustomerById(dto.getCustomerId())))
                .build();
    }

    @Override
    public List<AccountDTO> toDTOList(List<Account> accounts) {
        if (accounts == null) return List.of();
        return accounts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDTO> toResponseDTOList(List<Account> accounts) {
        if (accounts == null) return List.of();
        return accounts.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
