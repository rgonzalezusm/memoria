package cl.rgonzalez.memoria.ui.views.user;

import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import cl.rgonzalez.memoria.core.service.RSSrvUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@PageTitle("Usuarios")
@Route(value = "usuarios", layout = RSMainLayout.class)
@RolesAllowed("ADMIN")
public class RSViewUser extends VerticalLayout {

    private RSSrvUser srvUser;
    //
    private Grid<RSUser> grid;
    private Button buttonAdd = new Button("Agregar");
    private Button buttonEdit = new Button("Editar");
    private Button buttonDelete = new Button("Eliminar");

    public RSViewUser(RSSrvUser srvUser) {
        this.srvUser = srvUser;

        addClassName("default-view");
        add(createGridArea());

        buttonAdd.addClickListener(e -> addAction());
        buttonEdit.addClickListener(e -> editAction());
        buttonDelete.addClickListener(e -> deleteAction());
    }

    public Component createGridArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(createGrid());
        hl.add(createGridButtons());
        return hl;
    }

    public Component createGrid() {
        grid = new Grid<>();
        grid.setWidth("900px");
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(RSUser::getUsername).setHeader("Usuario");
        grid.addColumn(RSUser::getName).setHeader("Nombre");
        grid.addColumn(e -> Arrays.toString(e.getRoles().toArray())).setHeader("Roles");
        return grid;
    }

    public Component createGridButtons() {
        Div div = new Div();
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        div.add(buttonAdd);
        div.add(buttonEdit);
        div.add(buttonDelete);
        return div;
    }

    private void addAction() {
        UI.getCurrent().navigate("usuarios/agregar");
    }

    private void editAction() {
        Optional<RSUser> opt = grid.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione un usuario");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSUser.class, opt.get());
        UI.getCurrent().navigate("usuarios/editar");
    }

    private void deleteAction() {
        Optional<RSUser> opt = grid.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione un usuario");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSUser.class, opt.get());
        UI.getCurrent().navigate("usuarios/eliminar");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        List<RSUser> cats = srvUser.findAll();
        this.grid.setItems(cats);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
    }
}