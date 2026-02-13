package sn.xoslu.tech.ebank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.xoslu.tech.ebank.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
