package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cl.rgonzalez.memoria.core.entity.RSUser;

import java.util.Optional;

public interface RSRepoUser extends JpaRepository<RSUser, Long>, JpaSpecificationExecutor<RSUser> {

    Optional<RSUser> findByUsername(String username);
}
