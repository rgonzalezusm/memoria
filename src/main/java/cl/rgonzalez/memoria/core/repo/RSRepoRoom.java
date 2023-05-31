package cl.rgonzalez.memoria.core.repo;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RSRepoRoom extends JpaRepository<RSEntityRoom, Long>, JpaSpecificationExecutor<RSEntityRoom> {
}
