package sn.xoslu.tech.ebank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.xoslu.tech.ebank.entities.Account;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumero(String accountNumber);
}
