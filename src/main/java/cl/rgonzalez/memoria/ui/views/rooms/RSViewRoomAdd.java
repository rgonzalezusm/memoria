package cl.rgonzalez.memoria.ui.views.rooms;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.core.service.RSSrvUser;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import cl.rgonzalez.memoria.ui.views.user.RSViewUserForm;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@PageTitle("Salas > Agregar")
@Route(value = "salas/agregar", layout = RSMainLayout.class)
@RolesAllowed("ADMIN")
@Slf4j
public class RSViewRoomAdd extends VerticalLayout {

    private RSSrvRoom srvRoom;
    //
    private RSViewRoomForm formRoom;
    private Button buttonAdd = new Button("Agregar");
    private Button buttonBack = new Button("Volver");

    public RSViewRoomAdd(RSSrvRoom srvRoom) {
        this.srvRoom = srvRoom;
        addClassName("default-view");

        this.formRoom = new RSViewRoomForm(srvRoom);
        this.formRoom.setWidth("300px");

        add(formRoom);
        add(new HorizontalLayout(buttonAdd, buttonBack));

        buttonAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonAdd.addClickListener(e -> addAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("salas"));
    }

    private void addAction() {
        try {
            RSEntityRoom room = new RSEntityRoom();
            formRoom.getBinder().writeBean(room);

            srvRoom.save(room);
            RSFrontUtils.showInfo("Sala agregada correctamente");
            UI.getCurrent().navigate("salas");
        } catch (ValidationException e) {
            log.error("Error al crear sala", e);
            RSFrontUtils.showError("Error al crear sala");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
