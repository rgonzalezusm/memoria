package cl.rgonzalez.memoria.ui.views.products;

import cl.rgonzalez.memoria.core.RSCsvProductIO;
import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.service.RSSrvCategory;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@PageTitle("Productos")
@Route(value = "productos", layout = RSMainLayout.class)
@PermitAll
public class RSViewProducts extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvCategory srvCategory;
    //
    private TextField textSearch = new TextField("Buscar");
    private Button buttonSearch = new Button("Buscar");
    private Checkbox checkExact = new Checkbox("Búsqueda Exacta");
    private Grid<RSProduct> gridProducts;
    private Button buttonAdd = new Button("Agregar");
    private Button buttonEdit = new Button("Editar");
    private Button buttonDelete = new Button("Eliminar");
    private Button buttonImport = new Button("Importar");
    private Button buttonExport = new Button("Exportar");
    //
    private GridListDataView<RSProduct> gview = null;
    //    private List<TPProduct> products = Collections.emptyList();
    private Label labelTotal = new Label("Total: 0");
    private Set<RSProduct> selectedItems = new HashSet<>();

    public RSViewProducts(RSSrvProduct service, RSSrvCategory srvCategory) {
        this.srvProduct = service;
        this.srvCategory = srvCategory;

        addClassName("default-view");

        add(createSearchArea());
        add(createGridArea());
        add(createGridFooter());

        buttonSearch.addClickListener(e -> searchAction());
        buttonSearch.addClickShortcut(Key.ENTER);

        buttonAdd.addClickListener(e -> addProduct());
        buttonEdit.addClickListener(e -> editProduct());
        buttonDelete.addClickListener(e -> deleteProduct());
        buttonImport.addClickListener(e -> importProducts());
//        buttonExport.addClickListener(e -> exportProducts());
    }

    private Component createSearchArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.add(textSearch);
        hl.add(checkExact);
        hl.add(buttonSearch);

        textSearch.setWidth("30rem");
        buttonSearch.addClickShortcut(Key.ENTER);
        return hl;
    }

    private Component createGridArea() {
        HorizontalLayout div = new HorizontalLayout();
        div.add(createGrid());
        div.add(createGridButtons());
        return div;
    }

    private Component createGrid() {
        gridProducts = new Grid<>();
        gridProducts.setWidth("900px");
        gridProducts.setHeight("70vh");
        gridProducts.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        gridProducts.setSelectionMode(Grid.SelectionMode.SINGLE);

        gridProducts.addColumn(RSProduct::getCode).setHeader("Codigo").setWidth("150px").setSortable(true).setFooter(labelTotal);
        gridProducts.addColumn(RSProduct::getDescription).setHeader("Descripcion").setWidth("350px").setSortable(true);
        gridProducts.addColumn(e -> e.getCategory() != null ? e.getCategory().getName() : "").setHeader("Categoria").setWidth("80px").setSortable(true);
        gridProducts.addColumn(RSProduct::getSellPrice).setHeader("Precio").setWidth("80px").setSortable(true);

        GridContextMenu<RSProduct> gcm = gridProducts.addContextMenu();
        gcm.addItem("Agregar", e -> addProduct());
        gcm.addItem("Editar", e -> editProduct());
        gcm.addItem("Eliminar", e -> deleteProduct());

//        PersonContextMenu menu = new PersonContextMenu(gridProducts);
//        GridContextMenu<TPProduct> menu = gridProducts.addContextMenu();
//        menu.add(new Label("Acciones"));
//        menu.addItem("Agregar", e -> addProduct());
//        menu.addItem("Editar", e -> editProduct());
//        menu.addItem("Eliminar", e -> deleteProduct());
//        menu.add(new Label("Opciones avanzadas"));
//        menu.addItem("Importar", e -> importProducts());
//        menu.addItem("Exportar", e -> exportProducts());

        return gridProducts;
    }

    private Component createGridButtons() {
        Div hl = new Div();
        hl.setWidth("120px");
//        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        hl.add(buttonAdd);
        hl.add(buttonEdit);
        hl.add(buttonDelete);
//        d1.getChildren().forEach(c->c.getStyle().set("display", "block"));

        buttonDelete.getElement().getStyle().set("margin-bottom", "20px");

        Anchor anchor = new Anchor(new StreamResource("planilla.csv", this::getProductosCsvInputStream), null);
        anchor.setTarget("_blank");
        anchor.add(buttonExport);
        anchor.setWidth("100%");
        anchor.getElement().getStyle().set("margin-bottom", "20px");
//        anchor.getElement().getStyle().set("border", "1px solid red");
        buttonExport.setWidth("100%");

        hl.add(buttonImport);
        hl.add(anchor);

//        hl.add(new Button("multi", e -> gridProducts.setSelectionMode(Grid.SelectionMode.MULTI)));
//        hl.add(new Button("single", e -> gridProducts.setSelectionMode(Grid.SelectionMode.SINGLE)));

        return hl;
    }

    private Component createGridFooter() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(new Checkbox("Seleccion Múltiple", e -> {
            this.gridProducts.setSelectionMode(e.getValue() ? Grid.SelectionMode.MULTI : Grid.SelectionMode.SINGLE);
        }));
        return hl;
    }

    private void searchAction() {
        this.gview.refreshAll();
        this.labelTotal.setText("Total: " + gview.getItems().count());
    }

    private void addProduct() {
        UI.getCurrent().navigate("productos/agregar");
    }

    private void editProduct() {
        Set<RSProduct> list = gridProducts.getSelectionModel().getSelectedItems();
        if (list.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione un producto");
            return;
        }

        if (list.size() == 1) {
            ComponentUtil.setData(UI.getCurrent(), RSProduct.class, list.iterator().next());
            UI.getCurrent().navigate("productos/editar");
        } else {
            List<Long> ids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
            ComponentUtil.setData(UI.getCurrent(), Long[].class, ids.toArray(new Long[0]));
            UI.getCurrent().navigate("productos/editar_multiple");
        }

    }

    private void deleteProduct() {
        Set<RSProduct> list = gridProducts.getSelectionModel().getSelectedItems();
        if (list.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione un producto");
            return;
        }

        if (list.size() == 1) {
            ComponentUtil.setData(UI.getCurrent(), RSProduct.class, list.iterator().next());
            UI.getCurrent().navigate("productos/eliminar");
        } else {
            List<Long> ids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
            ComponentUtil.setData(UI.getCurrent(), Long[].class, ids.toArray(new Long[0]));
            UI.getCurrent().navigate("productos/eliminar_multiple");
        }
    }

    private void importProducts() {
        UI.getCurrent().navigate("productos/importar");
    }

    private InputStream getProductosCsvInputStream() {
        RSCsvProductIO exporter = new RSCsvProductIO();
        StringWriter sw = exporter.export(srvProduct);
        return IOUtils.toInputStream(sw.toString(), "UTF-8");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        updateProducts();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
    }

    private void updateProducts() {
        List<RSProduct> products = srvProduct.findAll();
        gview = this.gridProducts.setItems(products);
        gview.setFilter(p -> {
            if (textSearch.getValue() == null || textSearch.isEmpty()) {
                return true;
            }

            String strCat = p.getCategory() != null ? p.getCategory().getName() : "";
            String strPrice = p.getSellPrice() != null ? p.getSellPrice().toString() : "";
            String[] split = p.getDescription().split(" ");

            Pattern pattern = checkExact.getValue() ? Pattern.compile("(?i)" + textSearch.getValue()) : Pattern.compile("(?i).*" + textSearch.getValue() + ".*");
            boolean match = pattern.matcher(p.getCode()).matches();
            match = match || pattern.matcher(strCat).matches();
            match = match || pattern.matcher(strPrice).matches();

            for (String str : split) {
                match = match || pattern.matcher(str).matches();
            }
            return match;
        });
        this.labelTotal.setText("Total: " + products.size());
    }

//    private class PersonContextMenu extends GridContextMenu<TPProduct> {
//        public PersonContextMenu(Grid<TPProduct> target) {
//            super(target);
//
//            addItem("Agregar", e -> addProduct());
//            addItem("Editar", e -> editProduct());
//            addItem("Eliminar", e -> deleteProduct());
//            add(new Hr());
//
//            GridMenuItem<TPProduct> catItem = addItem("Cambiar Categoria");
//            GridSubMenu<TPProduct> subMenu = catItem.getSubMenu();
//
//            for (TPCategory c : srvCategory.findAll()) {
//                subMenu.addItem(c.getName(), e -> {
//                    if (!selectedItems.isEmpty()) {
//                        for (TPProduct p : selectedItems) {
//                            p.setCategory(c);
//                        }
////                        srvProduct.saveAll(selectedItems);
//                        srvProduct.updateCategories(selectedItems, c);
//                        selectedItems.clear();
//                        gview.refreshAll();
////                        updateProducts();
//                    }
//                });
//            }
//            subMenu.addItem(new Hr());
//            subMenu.addItem("Ninguno", e -> {
//                if (!selectedItems.isEmpty()) {
//                    for (TPProduct p : selectedItems) {
//                        p.setCategory(null);
//                    }
////                    srvProduct.saveAll(selectedItems);
//                    srvProduct.updateCategories(selectedItems, null);
//                    selectedItems.clear();
//                    gview.refreshAll();
////                    updateProducts();
//                }
//            });
//
////            setDynamicContentHandler(person -> {
////                // Do not show context menu when header is clicked
////                if (person == null)
////                    return false;
////                emailItem
////                        .setText(String.format("Email: %s", person.getEmail()));
////                phoneItem.setText(String.format("Call: %s",
////                        person.getAddress().getPhone()));
////                return true;
////            });
//        }
//    }
}
