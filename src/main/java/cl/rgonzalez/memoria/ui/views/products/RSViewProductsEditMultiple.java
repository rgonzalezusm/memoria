package cl.rgonzalez.memoria.ui.views.products;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.service.RSSrvCategory;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@PageTitle("Productos > Editar Varios")
@Route(value = "productos/editar_multiple", layout = RSMainLayout.class)
@PermitAll
public class RSViewProductsEditMultiple extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvCategory srvCategory;
    //
    private Label labelInfo = new Label("Productos a editar: 0");
    private ComboBox<RSCategory> comboCategory = new ComboBox<>("Categoria");
    //
    private Button buttonOk = new Button("Editar");
    private Button buttonBack = new Button("Volver");
    //
    private Long[] ids = null;

    public RSViewProductsEditMultiple(RSSrvProduct service, RSSrvCategory srvCategory) {
        this.srvProduct = service;
        this.srvCategory = srvCategory;

        addClassName("default-view");
        add(new HorizontalLayout(labelInfo));
        add(createForm());
        add(new HorizontalLayout(buttonOk, buttonBack));

        this.labelInfo.getElement().getStyle().set("font-weight", "bold");

        this.buttonOk.addClickListener(e -> editProductAction());
        this.buttonBack.addClickListener(e -> UI.getCurrent().navigate("productos"));

        buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private Component createForm() {
        FormLayout form = new FormLayout();
        form.add(comboCategory);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1), // Use one column by default
                new FormLayout.ResponsiveStep("800px", 2) // Use two columns, if layout's width exceeds 500px
        );
        form.setWidth("450px");
        form.getElement().getStyle().set("margin-bottom", "20px");

        this.comboCategory.setItems(srvCategory.findAll());
        this.comboCategory.setItemLabelGenerator(c -> c.getName());
        return form;
    }

    private void editProductAction() {
        RSCategory cat = comboCategory.getValue();
        if (cat != null) {
            srvProduct.editAllByIdsCategories(ids, cat);
            RSFrontendUtils.showInfo("Productos editados correctamente");
        }
        UI.getCurrent().navigate("productos");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        Long[] ids = ComponentUtil.getData(UI.getCurrent(), Long[].class);
        if (ids != null) {
            this.ids = ids;
            this.labelInfo.setText("Productos a editar: " + ids.length);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

}
