package cl.rgonzalez.memoria.ui.views.category;

import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.service.RSSrvCategory;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
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

@PageTitle("Categoria > Agregar")
@Route(value = "categorias/agregar", layout = RSMainLayout.class)
@PermitAll
public class RSViewCategoryAdd extends VerticalLayout {

    private RSSrvCategory srvCategory;
    private TextField textCategory = new TextField("Categoria");
    private Button buttonOk = new Button("Agregar");
    private Button buttonClear = new Button("Limpiar");
    private Button buttonBack = new Button("Volver");
    //
    private Binder<RSCategory> binder = new Binder(RSCategory.class);

    public RSViewCategoryAdd(RSSrvCategory srvCategory) {
        this.srvCategory = srvCategory;
        addClassName("default-view");

        add(textCategory);
        add(new HorizontalLayout(buttonOk, buttonClear, buttonBack));

        binder.forField(textCategory)
                .withValidator(name -> name != null, "Nombre nulo")
                .withValidator(name -> !name.isEmpty(), "Nombre vacio")
                .bind("name");

        textCategory.setWidth("30rem");
        buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonOk.addClickListener(e -> addAction());
        buttonClear.addClickListener(e -> clearAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("categorias"));
    }

    private void clearAction() {
        RSCategory cat = new RSCategory();
        binder.readBean(cat);
    }

    private void addAction() {
        try {
            RSCategory cat = new RSCategory();
            binder.writeBean(cat);
            srvCategory.save(cat);
            clearAction();
            RSFrontendUtils.showInfo("Categoria agregada correctamente");
            UI.getCurrent().navigate("categorias");
        } catch (ValidationException e) {
            RSFrontendUtils.showError("Error al editar categoria");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
