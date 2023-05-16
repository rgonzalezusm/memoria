package cl.rgonzalez.memoria.ui.views.category;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.service.RSSrvCategory;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Categoria > Eliminar")
@Route(value = "categorias/eliminar", layout = RSMainLayout.class)
@PermitAll
public class RSViewCategoryDelete extends VerticalLayout {

    private RSSrvCategory srvCategory;
    private TextField textCategory = new TextField("Categoria");
    private Button buttonDelete = new Button("Eliminar");
    private Button buttonBack = new Button("Volver");
    //
    private Binder<RSCategory> binder = new Binder(RSCategory.class);
    private RSCategory cat = null;

    public RSViewCategoryDelete(RSSrvCategory srvCategory) {
        this.srvCategory = srvCategory;
        addClassName("default-view");

        add(textCategory);
        add(new HorizontalLayout(buttonDelete, buttonBack));

        binder.forField(textCategory)
                .withValidator(name -> name != null, "Nombre nulo")
                .withValidator(name -> !name.isEmpty(), "Nombre vacio")
                .bind("name");

        textCategory.setWidth("30rem");
        textCategory.setReadOnly(true);

        buttonDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonDelete.addClickListener(e -> deleteAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("categorias"));
    }

    private void deleteAction() {
        if (cat == null) {
            return;
        }

        ConfirmDialog dialog = RSFrontendUtils.createConfirmDeleteDialog("La informacion relacionada con la categoria no podra ser recuperada. Desea continuar?");
        dialog.addConfirmListener(e -> {
            srvCategory.delete(cat);
            this.cat = null;
            clear();
            RSFrontendUtils.showInfo("Categoria eliminada");
            UI.getCurrent().navigate("categorias");
        });
        dialog.open();
    }

    private void clear() {
        this.textCategory.setValue("");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        cat = ComponentUtil.getData(UI.getCurrent(), RSCategory.class);
        binder.readBean(cat);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
