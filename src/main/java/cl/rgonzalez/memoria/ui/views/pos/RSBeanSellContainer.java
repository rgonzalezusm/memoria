package cl.rgonzalez.memoria.ui.views.pos;

import cl.rgonzalez.memoria.core.entity.RSSellUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSBeanSellContainer {

    private List<RSSellUnit> units;

}
