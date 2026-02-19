package sn.xoslu.tech.ebank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.xoslu.tech.ebank.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}