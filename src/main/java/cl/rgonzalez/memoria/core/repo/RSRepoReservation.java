package cl.rgonzalez.memoria.core.repo;

import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RSRepoReservation extends JpaRepository<RSEntityReservation, Long>, JpaSpecificationExecutor<RSEntityReservation> {

    @Query("SELECT r FROM RSEntityRoom WHERE r.room = ?1 AND year = ?2 AND semester = ?3 AND")
    public List<RSEntityReservation> findByRoomAndYearAndSemester(RSEntityRoom room, int year, int semester);
}
