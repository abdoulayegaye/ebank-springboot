package sn.xoslu.tech.ebank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.xoslu.tech.ebank.entities.Operation;

import java.time.Instant;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("SELECT o FROM Operation o WHERE o.account.numero = :accountNumber AND o.createdAt BETWEEN :start AND :end ORDER BY o.createdAt ASC")
    List<Operation> findByAccountAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}
