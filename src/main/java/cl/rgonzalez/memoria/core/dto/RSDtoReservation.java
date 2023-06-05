package cl.rgonzalez.memoria.core.dto;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSDtoReservation {

    private RSBlock block;
    private RSDayOfWeek day;

}
