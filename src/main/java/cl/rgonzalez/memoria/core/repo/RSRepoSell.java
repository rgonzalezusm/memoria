package cl.rgonzalez.memoria.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSSell;

import java.time.LocalDateTime;
import java.util.List;

public interface RSRepoSell extends JpaRepository<RSSell, Long>, JpaSpecificationExecutor<RSCategory> {

    @Query("SELECT s FROM RSSell s WHERE s.dateTime >= ?1 AND s.dateTime <= ?2")
    List<RSSell> findAllBeetween(LocalDateTime from, LocalDateTime to);
}
