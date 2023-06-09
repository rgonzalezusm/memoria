package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cl.rgonzalez.memoria.core.entity.RSEntityOptions;

public interface RSRepoOptions extends JpaRepository<RSEntityOptions, Long>, JpaSpecificationExecutor<RSEntityOptions> {

}
