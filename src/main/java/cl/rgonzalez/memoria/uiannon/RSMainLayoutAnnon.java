package cl.rgonzalez.memoria.uiannon;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
public class RSMainLayoutAnnon extends VerticalLayout implements RouterLayout {

    private Div top = new Div();

    public RSMainLayoutAnnon() {
        Image image = new Image("images/usm.png", "USM");
        image.setWidth("100px");

        this.top.setWidthFull();
        this.top.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.BETWEEN);

        this.top.add(image);
        this.top.add(new H2("Reserva de Salas Computacionales"));
        this.top.add(new Button("Administrador", e -> UI.getCurrent().navigate("admin/inicio")));
//        this.top.addClassNames(LumoUtility.Width.XLARGE, LumoUtility.Margin.AUTO);

        add(top);
        add(new Hr());
    }


}
