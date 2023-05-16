package cl.rgonzalez.memoria.ui.views;

import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
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

import java.util.Optional;

import cl.rgonzalez.memoria.ui.components.appnav.RSAppNav;
import cl.rgonzalez.memoria.ui.components.appnav.RSAppNavItem;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.ui.views.category.RSViewCategory;
import cl.rgonzalez.memoria.ui.views.options.RSViewOptions;
import cl.rgonzalez.memoria.ui.views.pos.RSViewPos;
import cl.rgonzalez.memoria.ui.views.products.RSViewProducts;
import cl.rgonzalez.memoria.ui.views.sells.RSViewSells;
import cl.rgonzalez.memoria.ui.views.user.RSViewUser;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class RSMainLayout extends AppLayout {

    private H2 viewTitle;

    private RSAuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public RSMainLayout(RSAuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
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

        if (accessChecker.hasAccess(RSViewPos.class)) {
            nav.addItem(new RSAppNavItem("Punto de Venta", RSViewPos.class, LineAwesomeIcon.CASH_REGISTER_SOLID.create()));
        }
        if (accessChecker.hasAccess(RSViewProducts.class)) {
            nav.addItem(new RSAppNavItem("Categorias", RSViewCategory.class, LineAwesomeIcon.FOLDER_SOLID.create()));
        }
        if (accessChecker.hasAccess(RSViewProducts.class)) {
            nav.addItem(new RSAppNavItem("Productos", RSViewProducts.class, LineAwesomeIcon.FILE_ARCHIVE_SOLID.create()));
        }
        if (accessChecker.hasAccess(RSViewSells.class)) {
            nav.addItem(new RSAppNavItem("Ventas", RSViewSells.class, LineAwesomeIcon.MONEY_BILL_SOLID.create()));
        }
        if (accessChecker.hasAccess(RSViewUser.class)) {
            nav.addItem(new RSAppNavItem("Usuarios", RSViewUser.class, LineAwesomeIcon.USERS_SOLID.create()));
        }
        if (accessChecker.hasAccess(RSViewOptions.class)) {
            nav.addItem(new RSAppNavItem("Opciones", RSViewOptions.class, LineAwesomeIcon.WRENCH_SOLID.create()));
        }


        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<RSUser> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            RSUser user = maybeUser.get();

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
