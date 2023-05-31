package cl.rgonzalez.memoria.ui.views.rooms;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@PageTitle("Salas > Eliminar")
@Route(value = "salas/eliminar", layout = RSMainLayout.class)
@RolesAllowed("ADMIN")
@Slf4j
public class RSViewRoomDelete extends VerticalLayout {

    private RSSrvRoom srvRoom;
    //
    private RSViewRoomForm formRoom;
    private Button buttonDelete = new Button("Eliminar");
    private Button buttonBack = new Button("Volver");
    //
    private RSEntityRoom room = null;

    public RSViewRoomDelete(RSSrvRoom srvRoom) {
        this.srvRoom = srvRoom;
        addClassName("default-view");

        this.formRoom = new RSViewRoomForm(srvRoom);
        this.formRoom.setWidth("300px");
        this.formRoom.getTextNumber().setReadOnly(true);
        this.formRoom.getTextName().setReadOnly(true);
        this.formRoom.getTextCapacity().setReadOnly(true);
        this.formRoom.getTextareaDescription().setReadOnly(true);

        add(formRoom);
        add(new HorizontalLayout(buttonDelete, buttonBack));

        buttonDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonDelete.addClickListener(e -> deleteAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("salas"));
    }

    private void deleteAction() {
        srvRoom.delete(room);
        RSFrontUtils.showInfo("Usuario eliminado");
        UI.getCurrent().navigate("salas");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        room = ComponentUtil.getData(UI.getCurrent(), RSEntityRoom.class);
        formRoom.getBinder().readBean(room);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
