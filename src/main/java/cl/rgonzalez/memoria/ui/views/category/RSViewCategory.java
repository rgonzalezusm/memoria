package cl.rgonzalez.memoria.ui.views.category;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.repo.RSRepoCategory;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@PageTitle("Categorias")
@Route(value = "categorias", layout = RSMainLayout.class)
@PermitAll
public class RSViewCategory extends VerticalLayout {

    private RSRepoCategory repo;
    //
    private Grid<RSCategory> gridCats;
    private Label labelTotal = new Label("Total: 0");
    private Button buttonAdd = new Button("Agregar");
    private Button buttonEdit = new Button("Editar");
    private Button buttonDelete = new Button("Eliminar");

    public RSViewCategory(RSRepoCategory repo) {
        this.repo = repo;

        addClassName("default-view");
        add(createGridArea());

        buttonAdd.addClickListener(e -> addCategoryAction());
        buttonEdit.addClickListener(e -> editCategoryAction());
        buttonDelete.addClickListener(e -> deleteCategoryAction());
    }

    public Component createGridArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(createGrid());
        hl.add(createGridButtons());
        return hl;
    }

    public Component createGrid() {
        gridCats = new Grid<>();
        gridCats.setWidth("300px");
        gridCats.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        gridCats.addColumn(RSCategory::getName).setHeader("Nombre").setFooter(labelTotal);
        return gridCats;
    }

    public Component createGridButtons() {
        Div div = new Div();
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        div.add(buttonAdd);
        div.add(buttonEdit);
        div.add(buttonDelete);
        return div;
    }

    private void addCategoryAction() {
        UI.getCurrent().navigate("categorias/agregar");
    }

    private void editCategoryAction() {
        Optional<RSCategory> opt = gridCats.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione una categoria");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSCategory.class, opt.get());
        UI.getCurrent().navigate("categorias/editar");
    }

    private void deleteCategoryAction() {
        Optional<RSCategory> opt = gridCats.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione una categoria");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSCategory.class, opt.get());
        UI.getCurrent().navigate("categorias/eliminar");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        List<RSCategory> cats = repo.findAll();
        this.gridCats.setItems(cats);
        this.labelTotal.setText("Total: " + cats.size());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
    }

}
