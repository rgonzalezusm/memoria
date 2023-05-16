package cl.rgonzalez.memoria.core.entity;

import cl.rgonzalez.memoria.core.RSPayStyle;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rs_sell")
@Data
public class RSSell extends RSAbstractEntity {

    @Column(nullable = false)
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tp_pay_style")
    private RSPayStyle payStyle;
    @Column(nullable = false)
    private Integer total = 0;
    @OneToMany(mappedBy = "sell")
    private List<RSSellUnit> units;
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private RSUser user;

    @Override
    public String toString() {
        return "TPSell{" +
                "dateTime=" + dateTime +
                ", total=" + total +
                '}';
    }

    public static RSSell build(LocalDateTime dateTime) {
        RSSell s = new RSSell();
        s.setDateTime(dateTime);
        return s;
    }
}
