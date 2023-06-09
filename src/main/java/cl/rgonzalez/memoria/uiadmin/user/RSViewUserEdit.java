package cl.rgonzalez.memoria.uiadmin.user;

import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.service.RSSrvUser;
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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@PageTitle("Usuarios > Editar")
@Route(value = "admin/usuarios/editar", layout = RSMainLayoutAnnon.class)
@RolesAllowed("ADMIN")
@Slf4j
public class RSViewUserEdit extends VerticalLayout {

    private RSSrvUser srvUser;
    //
    private RSViewUserForm formUser;
    private Button buttonEdit = new Button("Editar");
    private Button buttonBack = new Button("Volver");
    //
    private RSEntityUser user = null;

    public RSViewUserEdit(RSSrvUser srvUser) {
        this.srvUser = srvUser;
        addClassName("default-view");

        this.formUser = new RSViewUserForm(srvUser);
        this.formUser.setWidth("300px");

        add(formUser);
        add(new HorizontalLayout(buttonEdit, buttonBack));

        buttonEdit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonEdit.addClickListener(e -> editAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("usuarios"));
    }

    private void editAction() {
        try {
            formUser.getBinder().writeBean(user);
            user.setRoles(formUser.getRoles());
            srvUser.save(user);

            RSFrontUtils.showInfo("Usuario editado correctamente");
            UI.getCurrent().navigate("usuarios");
        } catch (ValidationException e) {
            log.error("Error al editar usuario", e);
            RSFrontUtils.showError("Error al editar usuario");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        user = ComponentUtil.getData(UI.getCurrent(), RSEntityUser.class);
        formUser.setUser(user);

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
