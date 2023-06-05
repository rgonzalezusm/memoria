package cl.rgonzalez.memoria.core.service;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSReservationType;
import cl.rgonzalez.memoria.core.dto.RSDtoReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.repo.RSRepoReservation;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
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
    RSRepoReservation repoReservation;

    public void saveSemestral(
            ZonedDateTime reservationDate, RSEntityUser user,
            RSEntityRoom room, int year, int semester,
            List<RSDtoReservation> reservations
    ) {

        List<RSEntityReservation> list = new ArrayList<>();
        for (RSDtoReservation res : reservations) {
            RSEntityReservation r = new RSEntityReservation();
            r.setReservationDate(reservationDate);
            r.setUser(user);
            r.setRoom(room);
            r.setBlock(res.getBlock().getValue());
            r.setType(RSReservationType.SEMESTRAL.getValue());
            r.setYear(year);
            r.setSemester(semester);
            r.setDayOfWeek(res.getDay().getValue());
            list.add(r);
        }

        repoReservation.saveAll(list);
    }

    public void saveEventual(
            ZonedDateTime reservationDate, RSEntityUser user,
            RSEntityRoom room, LocalDate date,
            List<RSBlock> blocks
    ) {
        List<RSEntityReservation> list = new ArrayList<>();
        for (RSBlock block : blocks) {
            RSEntityReservation r = new RSEntityReservation();
            r.setReservationDate(reservationDate);
            r.setUser(user);
            r.setRoom(room);
            r.setBlock(block.getValue());
            r.setType(RSReservationType.EVENTUAL.getValue());
            r.setYear(date.getYear());
            r.setSemester(RSFrontUtils.findSemester(date));
            r.setDayOfWeek(date.getDayOfWeek().getValue());
            r.setEventualMonth(date.getMonthValue());
            r.setEventualDay(date.getDayOfMonth());
            list.add(r);
        }
        repoReservation.saveAll(list);
    }

    private RSEntityReservation create(
            ZonedDateTime reservationDate,
            RSEntityUser user,
            RSEntityRoom room,
            RSBlock block,
            RSReservationType type
    ) {
        RSEntityReservation r = new RSEntityReservation();
        r.setReservationDate(reservationDate);
        r.setUser(user);
        r.setRoom(room);
        r.setBlock(block.getValue());
        r.setType(type.getValue());
        return r;
    }

    public List<RSEntityReservation> findAll(RSEntityRoom room, int year, int semester) {
        return repoReservation.findByRoomAndYearAndSemester(room, year, semester);
    }
}
