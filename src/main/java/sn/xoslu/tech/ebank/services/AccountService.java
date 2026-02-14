package sn.xoslu.tech.ebank.services;

import sn.xoslu.tech.ebank.dtos.AccountDTO;
import sn.xoslu.tech.ebank.dtos.AccountResponseDTO;

import java.util.List;

public interface AccountService {
    AccountResponseDTO createAccount(AccountDTO account);
    AccountResponseDTO getAccountById(Long id);
    AccountResponseDTO getAccountByNumber(String accountNumber);
    List<AccountResponseDTO> getAllAccounts();
    AccountDTO updateAccount(Long id, AccountDTO account);
    void closeAccount(Long id);
    void openAccount(Long id);
    double getBalance(String accountNumber);
}
