package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Table(name = "rs_reservation")
@Data
public class RSEntityReservation extends RSAbstractEntity {

    private ZonedDateTime reservationDate;

    @ManyToOne
    private RSEntityUser user;

    @ManyToOne
    private RSEntityRoom room;

    @Column(name="block")
    private Integer block;

    @Column(name="type")
    private Integer type;

    @Column(name="the_year")
    private Integer year;

    @Column(name="semester")
    private Integer semester;

    @Column(name="the_day_of_week")
    private Integer dayOfWeek;

    @Column(name="eventual_month")
    private Integer eventualMonth;

    @Column(name="eventual_day")
    private Integer eventualDay;
}
