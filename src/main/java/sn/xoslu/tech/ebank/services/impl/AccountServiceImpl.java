package sn.xoslu.tech.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xoslu.tech.ebank.dtos.AccountDTO;
import sn.xoslu.tech.ebank.dtos.AccountResponseDTO;
import sn.xoslu.tech.ebank.entities.Account;
import sn.xoslu.tech.ebank.entities.Customer;
import sn.xoslu.tech.ebank.exceptions.NotFoundException;
import sn.xoslu.tech.ebank.mappers.AccountMapper;
import sn.xoslu.tech.ebank.mappers.CustomerMapper;
import sn.xoslu.tech.ebank.repositories.AccountRepository;
import sn.xoslu.tech.ebank.services.AccountService;
import sn.xoslu.tech.ebank.services.CustomerService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Override
    public AccountResponseDTO createAccount(AccountDTO accountDTO) {
        Customer customer = customerMapper.toEntity(customerService.getCustomerById(accountDTO.getCustomerId()));
        Account account = accountMapper.toEntity(accountDTO);
        account.setNumero(UUID.randomUUID().toString());
        account.setCurrency("XOF");
        account.setActive(true);
        account.setCustomer(customer);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponseDTO(savedAccount);
    }

    @Override
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));
        return accountMapper.toResponseDTO(account);
    }

    @Override
    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByNumero(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found with number: " + accountNumber));
        return accountMapper.toResponseDTO(account);
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accountMapper.toResponseDTOList(accounts);
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));
        
        existingAccount.setBalance(accountDTO.getBalance());
        existingAccount.setActive(accountDTO.isActive());
        Account updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.toDTO(updatedAccount);
    }

    @Override
    public void closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));
        account.setActive(false);
        accountRepository.save(account);
    }

    @Override
    public void openAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));
        account.setActive(true);
        accountRepository.save(account);
    }

    @Override
    public double getBalance(String accountNumber) {
        Account account = accountRepository.findByNumero(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found with number: " + accountNumber));
        return account.getBalance();
    }
}
