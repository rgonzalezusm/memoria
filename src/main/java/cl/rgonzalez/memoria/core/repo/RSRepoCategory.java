package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cl.rgonzalez.memoria.core.entity.RSCategory;

public interface RSRepoCategory extends JpaRepository<RSCategory, Long>, JpaSpecificationExecutor<RSCategory> {

}
