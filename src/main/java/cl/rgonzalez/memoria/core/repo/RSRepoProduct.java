package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSProduct;

import java.util.Optional;
import java.util.Set;

public interface RSRepoProduct extends JpaRepository<RSProduct, Long>, JpaSpecificationExecutor<RSProduct> {

    public Optional<RSProduct> findByCode(String code);

    public Set<RSProduct> findAllByCategory(RSCategory category);

}
