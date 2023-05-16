package cl.rgonzalez.memoria.ui.views.products;

import cl.rgonzalez.memoria.core.service.RSSrvCategory;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Productos > Eliminar Varios")
@Route(value = "productos/eliminar_multiple", layout = RSMainLayout.class)
@PermitAll
public class RSViewProductsDeleteMultiple extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvCategory srvCategory;
    //
    private Label labelInfo = new Label("Productos a eliminar: 0");
    //
    private Button buttonOk = new Button("Eliminar");
    private Button buttonBack = new Button("Volver");
    //
    private Long[] ids = null;


    public RSViewProductsDeleteMultiple(RSSrvProduct service, RSSrvCategory srvCategory) {
        this.srvProduct = service;
        this.srvCategory = srvCategory;

        addClassName("default-view");
        add(new HorizontalLayout(labelInfo));
        add(new HorizontalLayout(buttonOk, buttonBack));

        this.labelInfo.getElement().getStyle().set("font-weight", "bold");

        this.buttonOk.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.buttonOk.addClickListener(e -> deleteProductAction());
        this.buttonBack.addClickListener(e -> UI.getCurrent().navigate("productos"));
    }

    private void deleteProductAction() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirmar Eliminacion");
        dialog.setText("La informacion relacionada con los productos no podra ser recuperada. Desea continuar?");
        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.setConfirmText("Eliminar");
        dialog.addConfirmListener(e -> {
            srvProduct.deleteAllByIds(ids);
            RSFrontendUtils.showInfo("Productos eliminados");
            dialog.close();
            UI.getCurrent().navigate("productos");
        });
        dialog.open();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        Long[] ids = ComponentUtil.getData(UI.getCurrent(), Long[].class);
        if (ids != null) {
            this.ids = ids;
            labelInfo.setText("Productos a eliminar: " + ids.length);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

}
