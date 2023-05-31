package cl.rgonzalez.memoria.core.dto;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSDtoReservationSemestralRow {

    private RSBlock block;
    private Map<RSDay, Boolean> map;
}
