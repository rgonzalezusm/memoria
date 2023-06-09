package cl.rgonzalez.memoria.uiadmin.user;

import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.RSFrontUtils;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
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
import cl.rgonzalez.memoria.core.service.RSSrvUser;

@PageTitle("Usuarios > Editar")
@Route(value = "admin/usuarios/eliminar", layout = RSMainLayoutAnnon.class)
@RolesAllowed("ADMIN")
@Slf4j
public class RSViewUserDelete extends VerticalLayout {

    private RSSrvUser srvUser;
    //
    private RSViewUserForm formUser;
    private Button buttonDelete = new Button("Eliminar");
    private Button buttonBack = new Button("Volver");
    //
    private RSEntityUser user = null;

    public RSViewUserDelete(RSSrvUser srvUser) {
        this.srvUser = srvUser;
        addClassName("default-view");

        this.formUser = new RSViewUserForm(srvUser);
        this.formUser.setWidth("300px");
        this.formUser.getTextUser().setReadOnly(true);
        this.formUser.getTextName().setReadOnly(true);
//        this.formUser.getCheckboxRoles().setReadOnly(true);

        add(formUser);
        add(new HorizontalLayout(buttonDelete, buttonBack));

        buttonDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonDelete.addClickListener(e -> deleteAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("usuarios"));
    }

    private void deleteAction() {
        srvUser.delete(user);
        RSFrontUtils.showInfo("Usuario eliminado");
        UI.getCurrent().navigate("usuarios");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        user = ComponentUtil.getData(UI.getCurrent(), RSEntityUser.class);
        formUser.getBinder().readBean(user);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
