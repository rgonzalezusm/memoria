package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "rs_category")
@Data
public class RSCategory extends RSAbstractEntity {

    @Column(name = "name", unique = true)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<RSProduct> products;

    public static RSCategory build(String name) {
        RSCategory c = new RSCategory();
        c.setName(name);
        return c;
    }
}
