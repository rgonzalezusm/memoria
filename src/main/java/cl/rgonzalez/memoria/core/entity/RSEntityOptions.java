package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "rs_options")
@Data
@EqualsAndHashCode(callSuper = true)
public class RSEntityOptions extends RSAbstractEntity {

    private String zone;

}
