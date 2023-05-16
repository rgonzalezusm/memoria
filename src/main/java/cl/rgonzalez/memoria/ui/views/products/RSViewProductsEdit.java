package cl.rgonzalez.memoria.ui.views.products;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.service.RSSrvCategory;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@PageTitle("Productos > Editar")
@Route(value = "productos/editar", layout = RSMainLayout.class)
@PermitAll
public class RSViewProductsEdit extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvCategory srvCategory;
    //
    private Binder<RSProduct> binder = new Binder<>(RSProduct.class);
    private TextField textCode = new TextField("Codigo");
    private TextField textDescription = new TextField("Descripcion");
    private ComboBox<RSCategory> comboCategory = new ComboBox<>("Categoria");
    private IntegerField textPrice = new IntegerField("Precio Venta");
    //
    private Button buttonOk = new Button("Editar");
    private Button buttonBack = new Button("Volver");
    //
    private RSProduct pr = null;

    public RSViewProductsEdit(RSSrvProduct service, RSSrvCategory srvCategory) {
        this.srvProduct = service;
        this.srvCategory = srvCategory;

        addClassName("default-view");
        add(createForm());
        add(new HorizontalLayout(buttonOk, buttonBack));

        this.buttonOk.addClickListener(e -> editProductAction());
        this.buttonBack.addClickListener(e -> UI.getCurrent().navigate("productos"));

        buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private Component createForm() {
        FormLayout form = new FormLayout();
        form.add(textCode, textDescription, comboCategory, textPrice);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1), // Use one column by default
                new FormLayout.ResponsiveStep("800px", 2) // Use two columns, if layout's width exceeds 500px
        );
        form.setWidth("450px");
        form.getElement().getStyle().set("margin-bottom", "20px");

        this.comboCategory.setItems(srvCategory.findAll());
        this.comboCategory.setItemLabelGenerator(c -> c.getName());
        this.textPrice.setValue(0);

        binder.forField(textCode)
                .withValidator(code -> code != null, "Codigo nulo")
                .withValidator(code -> !code.isEmpty(), "Codigo vacio")
                .withValidator(code -> {
                    if (!pr.getCode().equals(code)) {
                        return srvProduct.findByCode(code).isEmpty();
                    }
                    return true;
                }, "El codigo ya existe")
                .bind("code");
        binder.forField(textDescription)
                .withValidator(desc -> desc != null, "Descripcion nula")
                .withValidator(desc -> !desc.isEmpty(), "Descripcion vacia")
                .bind("description");
        binder.forField(comboCategory)
                .bind("category");
        binder.forField(textPrice)
                .withValidator(price -> price != null, "Precio nulo")
                .withValidator(price -> price >= 0, "Precio menor que cero")
                .bind("sellPrice");

        return form;
    }

    private void editProductAction() {
        try {
            binder.writeBean(pr);
            srvProduct.save(pr);
//            clear();
            RSFrontendUtils.showInfo("Producto editado correctamente");
            UI.getCurrent().navigate("productos");
        } catch (ValidationException e) {
            e.printStackTrace();
            RSFrontendUtils.showError("Error al crear producto");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        pr = ComponentUtil.getData(UI.getCurrent(), RSProduct.class);
        if (pr != null) {
            binder.readBean(pr);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

}
