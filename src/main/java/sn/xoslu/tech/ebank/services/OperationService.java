package sn.xoslu.tech.ebank.services;

import sn.xoslu.tech.ebank.dtos.OperationDTO;

import java.util.List;

public interface OperationService {
    void deposit(String accountNumber, double amount);
    void withdraw(String accountNumber, double amount);
    void transfer(String fromAccount, String toAccount, double amount);
    List<OperationDTO> getAccountOperations(String accountNumber);
}
