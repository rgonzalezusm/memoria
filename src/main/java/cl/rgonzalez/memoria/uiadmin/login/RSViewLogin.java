package cl.rgonzalez.memoria.uiadmin.login;

import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "admin/login")
public class RSViewLogin extends LoginOverlay implements BeforeEnterObserver {

    private final RSAuthenticatedUser authenticatedUser;

    public RSViewLogin(RSAuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n.Form form = new LoginI18n.Form();
        form.setTitle("");
        form.setUsername("Usuario");
        form.setPassword("Password");
        form.setSubmit("Ingresar");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Reserva");
        i18n.getHeader().setDescription("De Salas Computacionales");
        i18n.setAdditionalInformation(null);
        i18n.setForm(form);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
