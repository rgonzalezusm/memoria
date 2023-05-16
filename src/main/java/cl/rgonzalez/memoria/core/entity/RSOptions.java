package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rs_options")
@Data
public class RSOptions extends RSAbstractEntity {

    private String zone;

}
