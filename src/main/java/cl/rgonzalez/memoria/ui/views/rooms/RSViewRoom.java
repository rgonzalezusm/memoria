package cl.rgonzalez.memoria.ui.views.rooms;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
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

import java.util.List;
import java.util.Optional;

@PageTitle("Salas")
@Route(value = "salas_adm", layout = RSMainLayout.class)
@RolesAllowed("ADMIN")
public class RSViewRoom extends VerticalLayout {

    private RSSrvRoom srvRoom;
    private Grid<RSEntityRoom> grid;
    //
    private Button buttonAdd = new Button("Agregar");
    private Button buttonEdit = new Button("Editar");
    private Button buttonDelete = new Button("Eliminar");

    public RSViewRoom(RSSrvRoom srvRoom) {
        this.srvRoom = srvRoom;

        addClassName("default-view");
        add(createGridArea());

        buttonAdd.addClickListener(e -> addAction());
        buttonEdit.addClickListener(e -> editAction());
        buttonDelete.addClickListener(e -> deleteAction());
    }

    private Component createGridArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(createGrid());
        hl.add(createGridButtons());
        return hl;
    }

    private Component createGrid() {
        grid = new Grid<>();
        grid.setWidth("900px");
        grid.setHeight("55vh");
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(e -> e.getNumber()).setHeader("Nro").setWidth("80px");
        grid.addColumn(e -> e.getName()).setHeader("Nombre").setWidth("100px");
        grid.addColumn(e -> e.getCapacity()).setHeader("Capacidad").setWidth("120px");
        grid.addColumn(e -> e.getDescription()).setHeader("Descripcion").setWidth("850px");
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        List<RSEntityRoom> rooms = srvRoom.findAll();
        this.grid.setItems(rooms);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void addAction() {
        UI.getCurrent().navigate("salas/agregar");
    }

    private void editAction() {
        Optional<RSEntityRoom> opt = grid.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontUtils.showWarn("Seleccione una sala");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSEntityRoom.class, opt.get());
        UI.getCurrent().navigate("salas/editar");
    }

    private void deleteAction() {
        Optional<RSEntityRoom> opt = grid.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontUtils.showWarn("Seleccione una sala");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSEntityRoom.class, opt.get());
        UI.getCurrent().navigate("salas/eliminar");
    }

}
