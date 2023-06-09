package cl.rgonzalez.memoria.uiadmin.home;

import cl.rgonzalez.memoria.uiadmin.RSMainLayoutAdmin;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Inicio")
@Route(value = "admin/inicio", layout = RSMainLayoutAdmin.class)
@RolesAllowed("ADMIN")
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
