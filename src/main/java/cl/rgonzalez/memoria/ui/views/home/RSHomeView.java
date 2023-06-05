package cl.rgonzalez.memoria.ui.views.home;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.dto.RSDtoReservation;
import cl.rgonzalez.memoria.core.dto.RSDtoReservationSemestralRow;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.service.RSSrvReservation;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

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
