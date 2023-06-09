package cl.rgonzalez.memoria.core.repo;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RSRepoReservation extends JpaRepository<RSEntityReservation, Long>, JpaSpecificationExecutor<RSEntityReservation> {

    List<RSEntityReservation> findByYearAndSemester(Integer year, Integer semester);

    List<RSEntityReservation> findByRoomAndYearAndSemester(RSEntityRoom room, Integer year, Integer semester);

    @Query("SELECT r FROM RSEntityReservation r WHERE r.room = ?1 AND r.year = ?2 AND r.semester = ?3 AND r.block = ?4 AND r.semesterDayOfWeek = ?5 AND r.type=1")
    List<RSEntityReservation> findSemestralReservations(RSEntityRoom room, Integer year, Integer semester, Integer block, Integer dayOfWeek);

    @Query("SELECT r FROM RSEntityReservation r WHERE r.room = ?1 AND r.block = ?2 AND r.year = ?3 AND r.eventualMonth = ?4 AND r.eventualDay = ?5 AND r.type=2")
    List<RSEntityReservation> findEventualReservations(RSEntityRoom room, Integer block, Integer year, Integer month, Integer day);

}
