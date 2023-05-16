package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "rs_product")
@Data
public class RSProduct extends RSAbstractEntity {

    @Column(nullable = false, unique = true)
    private String code;
    private String description = "";
    private Integer buyPrice = 0;
    private Integer sellPrice = 0;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = true)
    private RSCategory category;

    @OneToMany(mappedBy = "product")
    private List<RSSellUnit> sellUnits;

    @Override
    public String toString() {
        return "TPProduct{" +
                "id=" + getId() +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }
}
