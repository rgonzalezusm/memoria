package cl.rgonzalez.memoria.ui.views.user;

import cl.rgonzalez.memoria.core.RSRole;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.service.RSSrvUser;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class RSViewUserForm extends FormLayout {

    private RSSrvUser srvUser;
    //
    private TextField textUser = new TextField("Usuario");
    private TextField textName = new TextField("Nombre");
    private CheckboxGroup<RSRole> checkboxRoles = new CheckboxGroup<>();
    //
    private RSUser user = new RSUser();
    private Binder<RSUser> binder = new Binder(RSUser.class);

    public RSViewUserForm(RSSrvUser srvUser) {
        this.srvUser = srvUser;

        checkboxRoles.setLabel("Roles");
        checkboxRoles.setItems(RSRole.values());
        checkboxRoles.select(RSRole.USER);
//        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        add(textUser);
        add(textName);
        add(checkboxRoles);

        binder.forField(textUser)
                .withValidator(username -> username != null, "Usuario nulo")
                .withValidator(username -> !username.isEmpty(), "Usuario vacio")
                .withValidator(username -> {
                    if (!user.getUsername().equals(username)) {
                        return srvUser.findByUsername(username).isEmpty();
                    }
                    return true;
                }, "El usuario ya existe")
                .bind("username");
        binder.forField(textName)
                .withValidator(name -> name != null, "Nombre nulo")
                .withValidator(name -> !name.isEmpty(), "Nombre vacio")
                .bind("name");
        binder.forField(checkboxRoles)
                .withValidator(roles -> !roles.isEmpty(), "Sin Roles")
                .bind("roles");
    }

    public void setUser(RSUser user) {
        this.user = user;
        this.binder.readBean(user);
    }

    public Binder<RSUser> getBinder() {
        return binder;
    }

    public TextField getTextUser() {
        return textUser;
    }

    public TextField getTextName() {
        return textName;
    }

    public CheckboxGroup<RSRole> getCheckboxRoles() {
        return checkboxRoles;
    }
}
