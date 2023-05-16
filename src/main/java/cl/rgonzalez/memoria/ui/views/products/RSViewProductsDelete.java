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
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Productos > Eliminar")
@Route(value = "productos/eliminar", layout = RSMainLayout.class)
@PermitAll
public class RSViewProductsDelete extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvCategory srvCategory;
    //
    private Binder<RSProduct> binder = new Binder<>(RSProduct.class);
    private TextField textCode = new TextField("Codigo");
    private TextField textDescription = new TextField("Descripcion");
    private ComboBox<RSCategory> comboCategory = new ComboBox<>("Categoria");
    private IntegerField textPrice = new IntegerField("Precio Venta");
    //
    private Button buttonOk = new Button("Eliminar");
    private Button buttonBack = new Button("Volver");
    //
    private RSProduct pr = null;

    public RSViewProductsDelete(RSSrvProduct service, RSSrvCategory srvCategory) {
        this.srvProduct = service;
        this.srvCategory = srvCategory;

        addClassName("default-view");
        add(createForm());
        add(new HorizontalLayout(buttonOk, buttonBack));

        this.buttonOk.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.buttonOk.addClickListener(e -> deleteProductAction());
        this.buttonBack.addClickListener(e -> UI.getCurrent().navigate("productos"));
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

        List<RSCategory> cats = srvCategory.findAll();
        this.comboCategory.setItems(cats);
        this.comboCategory.setItemLabelGenerator(c -> c.getName());

        binder.forField(textCode).bind("code");
        binder.forField(textDescription).bind("description");
        binder.forField(comboCategory).bind("category");
        binder.forField(textPrice).bind("sellPrice");

        textCode.setReadOnly(true);
        textDescription.setReadOnly(true);
        comboCategory.setReadOnly(true);
        textPrice.setReadOnly(true);

        return form;
    }

    private void deleteProductAction() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirmar Eliminacion");
        dialog.setText("La informacion relacionada con el producto no podra ser recuperada. Desea continuar?");
        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.setConfirmText("Eliminar");
        dialog.addConfirmListener(e -> {
            srvProduct.delete(pr);
            pr = null;
            clear();
            RSFrontendUtils.showInfo("Producto eliminado");
            dialog.close();
            UI.getCurrent().navigate("productos");
        });
        dialog.open();
    }

    private void clear() {
        this.textCode.setValue("");
        this.textCode.setErrorMessage("");
        this.textCode.setInvalid(false);

        this.textDescription.setValue("");
        this.textDescription.setErrorMessage("");
        this.textDescription.setInvalid(false);

        this.textPrice.setValue(null);
        this.textPrice.setErrorMessage("");
        this.textPrice.setInvalid(false);

        this.comboCategory.setValue(null);
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
