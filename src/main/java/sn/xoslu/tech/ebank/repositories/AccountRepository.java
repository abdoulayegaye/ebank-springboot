package sn.xoslu.tech.ebank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.xoslu.tech.ebank.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
