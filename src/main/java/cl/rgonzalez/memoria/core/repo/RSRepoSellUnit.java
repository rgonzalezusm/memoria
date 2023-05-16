package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSSell;
import cl.rgonzalez.memoria.core.entity.RSSellUnit;

import java.util.List;

public interface RSRepoSellUnit extends JpaRepository<RSSellUnit, Long>, JpaSpecificationExecutor<RSCategory> {

    List<RSSellUnit> findAllBySell(RSSell sell);
}
