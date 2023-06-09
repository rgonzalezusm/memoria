package cl.rgonzalez.memoria.uiadmin.rooms;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.RSFrontUtils;
import cl.rgonzalez.memoria.uiadmin.RSMainLayoutAdmin;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
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

@PageTitle("Salas > Editar")
@Route(value = "admin/salas/editar", layout = RSMainLayoutAdmin.class)
@RolesAllowed("ADMIN")
@Slf4j
public class RSViewRoomEdit extends VerticalLayout {

    private RSSrvRoom srvRoom;
    //
    private RSViewRoomForm formRoom;
    private Button buttonEdit = new Button("Editar");
    private Button buttonBack = new Button("Volver");
    //
    private RSEntityRoom room = null;

    public RSViewRoomEdit(RSSrvRoom srvRoom) {
        this.srvRoom = srvRoom;
        addClassName("default-view");

        this.formRoom = new RSViewRoomForm(srvRoom);
        this.formRoom.setWidth("300px");

        add(formRoom);
        add(new HorizontalLayout(buttonEdit, buttonBack));

        buttonEdit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonEdit.addClickListener(e -> editAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("salas"));
    }

    private void editAction() {
        try {
            formRoom.getBinder().writeBean(room);

            RSFrontUtils.showInfo("Sala editada correctamente");
            UI.getCurrent().navigate("salas");
        } catch (ValidationException e) {
            log.error("Error al editar sala", e);
            RSFrontUtils.showError("Error al editar sala");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        room = ComponentUtil.getData(UI.getCurrent(), RSEntityRoom.class);
        formRoom.setUser(room);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
