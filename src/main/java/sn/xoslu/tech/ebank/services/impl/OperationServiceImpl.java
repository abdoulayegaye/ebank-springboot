package sn.xoslu.tech.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xoslu.tech.ebank.dtos.OperationDTO;
import sn.xoslu.tech.ebank.entities.Account;
import sn.xoslu.tech.ebank.entities.Operation;
import sn.xoslu.tech.ebank.enums.TypeOperation;
import sn.xoslu.tech.ebank.exceptions.NotFoundException;
import sn.xoslu.tech.ebank.mappers.OperationMapper;
import sn.xoslu.tech.ebank.repositories.AccountRepository;
import sn.xoslu.tech.ebank.repositories.OperationRepository;
import sn.xoslu.tech.ebank.services.OperationService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    @Override
    public void deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByNumero(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        
        Operation operation = new Operation();
        operation.setNumero(UUID.randomUUID().toString());
        operation.setType(TypeOperation.DEPOSIT);
        operation.setAmount(amount);
        operation.setAccount(account);
        operationRepository.save(operation);
        
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByNumero(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        Operation operation = new Operation();
        operation.setNumero(UUID.randomUUID().toString());
        operation.setType(TypeOperation.WITHDRAWAL);
        operation.setAmount(amount);
        operation.setAccount(account);
        operationRepository.save(operation);

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, double amount) {
        withdraw(fromAccount, amount);
        deposit(toAccount, amount);
    }

    @Override
    public List<OperationDTO> getAccountOperations(String accountNumber) {
        Account account = accountRepository.findByNumero(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return operationMapper.toDTOList(account.getOperations());
    }
}
