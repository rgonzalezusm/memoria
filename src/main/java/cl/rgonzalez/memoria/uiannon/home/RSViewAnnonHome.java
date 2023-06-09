package cl.rgonzalez.memoria.uiannon.home;

import cl.rgonzalez.memoria.core.RSReservationType;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Inicio")
@Route(value = "inicio", layout = RSMainLayoutAnnon.class)
@RouteAlias(value = "", layout = RSMainLayoutAnnon.class)
@AnonymousAllowed
public class RSViewAnnonHome extends Div {

    private ComboBox<RSReservationType> comboSelection = new ComboBox<>();
    private Button buttonReserve = new Button("Continuar");

    public RSViewAnnonHome() {
        setWidth("400px");
        getElement().getStyle().set("margin", "auto");

        addClassName(LumoUtility.Display.FLEX);
        addClassName(LumoUtility.FlexDirection.COLUMN);
        addClassName(LumoUtility.JustifyContent.CENTER);

        add(new H3("Seleccione el tipo de reserva"));
        add(createArea());
    }

    private Component createArea() {
        comboSelection.setItems(RSReservationType.values());
        comboSelection.setValue(RSReservationType.SEMESTRAL);
        comboSelection.setItemLabelGenerator(RSReservationType::getName);

        buttonReserve.addClickListener(e -> reserveAction());

        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        hl.add(comboSelection);
        hl.add(buttonReserve);

        return hl;
    }

    private void reserveAction() {
        if (comboSelection.getValue().equals(RSReservationType.SEMESTRAL)) {
            UI.getCurrent().navigate("semestral");
        } else {
            UI.getCurrent().navigate("eventual");
        }

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
