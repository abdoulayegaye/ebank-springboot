package sn.xoslu.tech.ebank.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.xoslu.tech.ebank.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
        SELECT c FROM Customer c
        WHERE LOWER(c.name)  LIKE LOWER(CONCAT('%', :query, '%'))
        OR    LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
        OR    CAST(c.id AS string) LIKE CONCAT('%', :query, '%')
    """)
    Page<Customer> searchWithPagination(
            @Param("query") String query,
            Pageable pageable
    );
}
