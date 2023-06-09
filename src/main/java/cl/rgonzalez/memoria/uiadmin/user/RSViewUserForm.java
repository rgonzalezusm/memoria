package cl.rgonzalez.memoria.uiadmin.user;

import cl.rgonzalez.memoria.core.RSRole;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.service.RSSrvUser;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.HashSet;
import java.util.Set;

public class RSViewUserForm extends FormLayout {

    private RSSrvUser srvUser;
    //
    private TextField textUser = new TextField("Usuario");
    private TextField textName = new TextField("Nombre");
    private RadioButtonGroup<RSRole> radioGroupRoles = new RadioButtonGroup<>();
    //
    private RSEntityUser user = new RSEntityUser();
    private Binder<RSEntityUser> binder = new Binder(RSEntityUser.class);

    public RSViewUserForm(RSSrvUser srvUser) {
        this.srvUser = srvUser;

        radioGroupRoles.setLabel("Roles");
        radioGroupRoles.setItems(RSRole.values());
        radioGroupRoles.setValue(RSRole.USER);
//        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        add(textUser);
        add(textName);
        add(radioGroupRoles);

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
    }

    public void setUser(RSEntityUser user) {
        this.user = user;
        this.binder.readBean(user);
        radioGroupRoles.setValue(user.getRoles().contains(RSRole.ADMIN) ? RSRole.ADMIN : RSRole.USER);
    }

    public Binder<RSEntityUser> getBinder() {
        return binder;
    }

    public TextField getTextUser() {
        return textUser;
    }

    public TextField getTextName() {
        return textName;
    }

    public Set<RSRole> getRoles() {
        Set<RSRole> roles = new HashSet<>();
        RSRole rol = radioGroupRoles.getValue();
        roles.add(rol);
        if (rol.equals(RSRole.ADMIN)) {
            roles.add(RSRole.USER);
        }
        return roles;
    }


}
