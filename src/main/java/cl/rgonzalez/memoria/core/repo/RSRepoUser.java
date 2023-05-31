package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;

import java.util.Optional;

public interface RSRepoUser extends JpaRepository<RSEntityUser, Long>, JpaSpecificationExecutor<RSEntityUser> {

    Optional<RSEntityUser> findByUsername(String username);
}
