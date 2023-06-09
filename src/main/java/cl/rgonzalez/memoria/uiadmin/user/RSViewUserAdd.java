package cl.rgonzalez.memoria.uiadmin.user;

import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.RSFrontUtils;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
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
import cl.rgonzalez.memoria.core.service.RSSrvUser;

@PageTitle("Usuarios > Agregar")
@Route(value = "admin/usuarios/agregar", layout = RSMainLayoutAnnon.class)
@RolesAllowed("ADMIN")
@Slf4j
public class RSViewUserAdd extends VerticalLayout {

    private RSSrvUser srvUser;
    //
    private RSViewUserForm formUser;
    private Button buttonAdd = new Button("Agregar");
    private Button buttonBack = new Button("Volver");

    public RSViewUserAdd(RSSrvUser srvUser) {
        this.srvUser = srvUser;
        addClassName("default-view");

        this.formUser = new RSViewUserForm(srvUser);
        this.formUser.setWidth("300px");

        add(formUser);
        add(new HorizontalLayout(buttonAdd, buttonBack));

        buttonAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonAdd.addClickListener(e -> addAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("usuarios"));
    }

    private void addAction() {
        try {
            RSEntityUser user = new RSEntityUser();
            formUser.getBinder().writeBean(user);
            user.setRoles(formUser.getRoles());

            srvUser.create(user);
            RSFrontUtils.showInfo("Usuario agregado correctamente");
            UI.getCurrent().navigate("usuarios");
        } catch (ValidationException e) {
            log.error("Error al crear usuario", e);
            RSFrontUtils.showError("Error al crear usuario");
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
