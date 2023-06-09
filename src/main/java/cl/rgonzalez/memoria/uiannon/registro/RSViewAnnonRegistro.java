package cl.rgonzalez.memoria.uiannon.registro;

import cl.rgonzalez.memoria.core.RSReservationType;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Solicitud Enviada")
@Route(value = "solicitud_enviada", layout = RSMainLayoutAnnon.class)
@AnonymousAllowed
public class RSViewAnnonRegistro extends Div {

    public RSViewAnnonRegistro() {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
