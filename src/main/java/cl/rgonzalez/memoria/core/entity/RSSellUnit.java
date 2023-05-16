package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rs_sell_unit")
@Data
public class RSSellUnit extends RSAbstractEntity {

    private Double amount;
    private Integer pricePerUnit;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private RSProduct product;

    @ManyToOne
    @JoinColumn(name = "idSell")
    private RSSell sell;

}
