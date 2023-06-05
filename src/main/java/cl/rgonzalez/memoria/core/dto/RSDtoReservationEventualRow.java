package cl.rgonzalez.memoria.core.dto;

import cl.rgonzalez.memoria.core.RSBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSDtoReservationEventualRow {

    private RSBlock block;
    private Boolean reserved;
}
