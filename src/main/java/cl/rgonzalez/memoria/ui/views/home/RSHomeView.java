package cl.rgonzalez.memoria.ui.views.home;

import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

@PageTitle("Inicio")
@Route(value = "inicio", layout = RSMainLayout.class)
@RouteAlias(value = "", layout = RSMainLayout.class)
@PermitAll
public class RSHomeView extends VerticalLayout {

    public RSHomeView() {
        add(new H2("Reserva de Salas Computacionales"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
