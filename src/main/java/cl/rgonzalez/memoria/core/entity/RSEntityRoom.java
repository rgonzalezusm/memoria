package cl.rgonzalez.memoria.core.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rs_room")
@Data
public class RSEntityRoom extends RSAbstractEntity {

    @Column(nullable = false, unique = true)
    private Integer number;
    private String name = "";
    private Integer capacity = 30;
    private String description = "";

    public String format() {
        return name + " (" + capacity + ")";
    }

    public static RSEntityRoom build(int number, String name, int capacity, String description) {
        RSEntityRoom room = new RSEntityRoom();
        room.setNumber(number);
        room.setName(name);
        room.setCapacity(capacity);
        room.setDescription(description);
        return room;

    }

    @Override
    public String toString() {
        return "RSSala{" +
                "id=" + getId() +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}
