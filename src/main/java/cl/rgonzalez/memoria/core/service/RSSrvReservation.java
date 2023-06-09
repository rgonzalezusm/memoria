package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.RSReservationType;
import cl.rgonzalez.memoria.core.dto.RSDtoBlockAndDay;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.repo.RSRepoReservation;
import cl.rgonzalez.memoria.RSFrontUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RSSrvReservation {

    @Autowired
    RSRepoReservation repo;

    public void saveSemestral(
            String name, String course, Integer parallel,
            ZonedDateTime reservationDate,
            RSEntityRoom room, int year, int semester,
            List<RSDtoBlockAndDay> reservations
    ) {

        List<RSEntityReservation> list = new ArrayList<>();
        for (RSDtoBlockAndDay res : reservations) {
            RSEntityReservation r = new RSEntityReservation();
            r.setType(RSReservationType.SEMESTRAL.getValue());
            r.setReservationDate(reservationDate);
            r.setName(name);
            r.setCourse(course);
            r.setParallel(parallel);
            r.setRoom(room);
            r.setBlock(res.getBlock().getValue());
            r.setYear(year);
            r.setSemester(semester);
            r.setSemesterDayOfWeek(res.getDay().getValue());
            r.setEventualMonth(null);
            r.setEventualDay(null);
            list.add(r);
        }

        repo.saveAll(list);
    }

    public void saveEventual(
            String name, String course, Integer parallel,
            ZonedDateTime reservationDate, RSEntityUser user,
            RSEntityRoom room, LocalDate date, List<RSBlock> blocks
    ) {
        List<RSEntityReservation> list = new ArrayList<>();
        for (RSBlock block : blocks) {


            RSEntityReservation r = new RSEntityReservation();
            r.setType(RSReservationType.EVENTUAL.getValue());
            r.setReservationDate(reservationDate);
            r.setName(name);
            r.setCourse(course);
            r.setParallel(parallel);
            r.setRoom(room);
            r.setBlock(block.getValue());
            r.setYear(date.getYear());
            r.setSemester(RSFrontUtils.findSemester(date));
            r.setSemesterDayOfWeek(null);
            r.setEventualMonth(date.getMonthValue());
            r.setEventualDay(date.getDayOfMonth());
            list.add(r);
        }
        repo.saveAll(list);
    }

    public List<RSEntityReservation> findByRoomAndSemester(RSEntityRoom room, Integer year, Integer semester) {
        return repo.findByRoomAndYearAndSemester(room, year, semester);
    }

    public List<RSEntityReservation> findBySemester(Integer year, Integer semester) {
        return repo.findByYearAndSemester(year, semester);
    }

    public List<RSEntityReservation> findSemestralReservations(
            RSEntityRoom room, int year, Integer semester, List<RSDtoBlockAndDay> reservations
    ) {
        List<RSEntityReservation> list = new ArrayList<>();
        for (RSDtoBlockAndDay r : reservations) {
            RSBlock block = r.getBlock();
            RSDayOfWeek day = r.getDay();
            List<RSEntityReservation> existing = repo.findSemestralReservations(room, year, semester, block.getValue(), day.getValue());
            list.addAll(existing);
        }
        return list;
    }

    public List<RSEntityReservation> findEventualReservations(
            RSEntityRoom room, LocalDate reservationDate, List<RSBlock> blocks
    ) {
        int year = reservationDate.getYear();
        int month = reservationDate.getMonthValue();
        int day = reservationDate.getDayOfMonth();

        List<RSEntityReservation> list = new ArrayList<>();
        for (RSBlock block : blocks) {
            List<RSEntityReservation> existing = repo.findEventualReservations(room, block.getValue(), year, month, day);
            list.addAll(existing);
        }
        return list;
    }


}
