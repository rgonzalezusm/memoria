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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Categoria > Editar")
@Route(value = "categorias/editar", layout = RSMainLayout.class)
@PermitAll
public class RSViewCategoryEdit extends VerticalLayout {

    private RSSrvCategory srvCategory;
    private TextField textCategory = new TextField("Categoria");
    private Button buttonEdit = new Button("Editar");
    private Button buttonBack = new Button("Volver");
    //
    private Binder<RSCategory> binder = new Binder(RSCategory.class);
    private RSCategory cat = null;

    public RSViewCategoryEdit(RSSrvCategory srvCategory) {
        this.srvCategory = srvCategory;
        addClassName("default-view");

        add(textCategory);
        add(new HorizontalLayout(buttonEdit, buttonBack));

        binder.forField(textCategory)
                .withValidator(name -> name != null, "Nombre nulo")
                .withValidator(name -> !name.isEmpty(), "Nombre vacio")
                .bind("name");

        textCategory.setWidth("30rem");
        buttonEdit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonEdit.addClickListener(e -> editAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("categorias"));
    }

    private void editAction() {
        if (cat == null) {
            return;
        }

        try {
            binder.writeBean(cat);
            srvCategory.save(cat);
            RSFrontendUtils.showInfo("Categoria editada correctamente");
            UI.getCurrent().navigate("categorias");
        } catch (ValidationException e) {
            RSFrontendUtils.showError("Error al editar categoria");
        }
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
