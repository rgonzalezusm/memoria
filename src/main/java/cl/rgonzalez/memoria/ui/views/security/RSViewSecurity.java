package cl.rgonzalez.memoria.ui.views.security;

import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.service.RSSrvUser;
import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@PageTitle("Seguridad")
@Route(value = "seguridad", layout = RSMainLayout.class)
@PermitAll
public class RSViewSecurity extends VerticalLayout {

    private RSAuthenticatedUser authenticatedUser;
    private PasswordEncoder passwordEncoder;
    private RSSrvUser srvUser;
    //
    private PasswordField textPass01 = new PasswordField("Ingrese Nuevo Password");
    private PasswordField textPass02 = new PasswordField("Reingrese Password");
    private Button buttonChangePassword = new Button("Cambiar Password");
    ;

    public RSViewSecurity(RSAuthenticatedUser authenticatedUser, PasswordEncoder passwordEncoder, RSSrvUser srvUser) {
        this.authenticatedUser = authenticatedUser;
        this.passwordEncoder = passwordEncoder;
        this.srvUser = srvUser;
        addClassName("default-view");

        FormLayout fl = new FormLayout();
        fl.add(textPass01);
        fl.add(textPass02);
        add(fl);

        fl.setWidth("400px");
        add(buttonChangePassword);

        buttonChangePassword.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonChangePassword.addClickListener(e -> changePassword());
    }

    private void changePassword() {
        Optional<RSUser> opt = authenticatedUser.get();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Sin usuario");
            return;
        }

        String value1 = textPass01.getValue();
        String value2 = textPass02.getValue();

        if (value1.equals(value2)) {
            RSUser user = opt.get();
            user.setHashedPassword(passwordEncoder.encode(value1));
            srvUser.save(user);
            RSFrontendUtils.showInfo("Password Editado correctamente");
            UI.getCurrent().navigate("pos");
        } else {
            textPass01.clear();
            textPass02.clear();
            RSFrontendUtils.showWarn("Los Passwords no coinciden");
        }
    }
}
