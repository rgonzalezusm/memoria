package cl.rgonzalez.memoria.uiadmin;

import cl.rgonzalez.memoria.core.RSRole;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
import cl.rgonzalez.memoria.components.appnav.RSAppNav;
import cl.rgonzalez.memoria.components.appnav.RSAppNavItem;
import cl.rgonzalez.memoria.uiadmin.home.RSHomeView;
import cl.rgonzalez.memoria.uiadmin.options.RSViewOptions;
import cl.rgonzalez.memoria.uiadmin.reservations.RSReservationsView;
import cl.rgonzalez.memoria.uiadmin.rooms.RSViewRoom;
import cl.rgonzalez.memoria.uiannon.rooms.RSViewRoomForUser;
import cl.rgonzalez.memoria.uiadmin.user.RSViewUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class RSMainLayoutAdmin extends AppLayout {

    private H2 viewTitle;

    private RSAuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public RSMainLayoutAdmin(RSAuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames("MainLayout-viewTitle");
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

//        Div divHeader = new Div(viewTitle, new RSDateTimeContainer());
        Div divHeader = new Div(viewTitle);
        divHeader.addClassName("MainLayout-divHeader");
        addToNavbar(true, toggle, divHeader);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Reserva");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, scroller, createFooter());
    }

    private RSAppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        RSAppNav nav = new RSAppNav();

        if (accessChecker.hasAccess(RSHomeView.class)) {
            nav.addItem(new RSAppNavItem("Inicio", RSHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

//        if (accessChecker.hasAccess(RSReservationSemesterView.class)) {
//            nav.addItem(new RSAppNavItem("Reserva Semestral", RSReservationSemesterView.class, LineAwesomeIcon.HAND_POINT_UP_SOLID.create()));
//        }
//        if (accessChecker.hasAccess(RSReservationEventualView.class)) {
//            nav.addItem(new RSAppNavItem("Reserva Eventual", RSReservationEventualView.class, LineAwesomeIcon.HAND_POINT_RIGHT_SOLID.create()));
//        }

        RSEntityUser user = authenticatedUser.get().orElseThrow();
        if (user.getRoles().contains(RSRole.ADMIN)) {
            if (accessChecker.hasAccess(RSViewRoom.class)) {
                nav.addItem(new RSAppNavItem("Salas", RSViewRoom.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
            }
        } else {
            if (accessChecker.hasAccess(RSViewRoomForUser.class)) {
                nav.addItem(new RSAppNavItem("Salas", RSViewRoomForUser.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
            }
        }

        if (accessChecker.hasAccess(RSReservationsView.class)) {
            nav.addItem(new RSAppNavItem("Reservas", RSReservationsView.class, LineAwesomeIcon.COMPASS_SOLID.create()));
        }

        if (accessChecker.hasAccess(RSViewUser.class)) {
            nav.addItem(new RSAppNavItem("Usuarios", RSViewUser.class, LineAwesomeIcon.USERS_SOLID.create()));
        }
//        if (accessChecker.hasAccess(RSViewSecurity.class)) {
//            nav.addItem(new RSAppNavItem("Seguridad", RSViewSecurity.class, LineAwesomeIcon.KEY_SOLID.create()));
//        }
        if (accessChecker.hasAccess(RSViewOptions.class)) {
            nav.addItem(new RSAppNavItem("Opciones", RSViewOptions.class, LineAwesomeIcon.WRENCH_SOLID.create()));
        }


        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<RSEntityUser> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            RSEntityUser user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
//            StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(user.getProfilePicture()));
//            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Cambiar Password", e -> {
                UI.getCurrent().navigate("seguridad");
            });
            userName.getSubMenu().addItem("Salir", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

}
