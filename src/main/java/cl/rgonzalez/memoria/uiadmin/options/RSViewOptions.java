package cl.rgonzalez.memoria.uiadmin.options;

import cl.rgonzalez.memoria.exceptions.RSException;
import cl.rgonzalez.memoria.core.service.RSSrvOptions;
import cl.rgonzalez.memoria.RSFrontUtils;
import cl.rgonzalez.memoria.uiadmin.RSMainLayoutAdmin;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Opciones")
@Route(value = "admin/opciones", layout = RSMainLayoutAdmin.class)
@RolesAllowed("ADMIN")
public class RSViewOptions extends VerticalLayout {

    private RSSrvOptions srvOptions;
    private TextField textZone = new TextField("Zona Horaria");
    private Button buttonOk = new Button("Actualizar");

    public RSViewOptions(RSSrvOptions srvOptions) {
        this.srvOptions = srvOptions;
        addClassName("default-view");

        FormLayout fl = new FormLayout();
        fl.add(textZone);
        add(fl);

        fl.setWidth("400px");
        add(buttonOk);

        buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonOk.addClickListener(e -> updateOptions());
    }

    private void updateOptions() {
        String txtZone = textZone.getValue();
        if (txtZone == null || txtZone.isEmpty()) {
            textZone.clear();
            RSFrontUtils.showWarn("Ingrese zona horaria valida");
            return;
        }

        try {
            srvOptions.setZone(txtZone);
            RSFrontUtils.showWarn("Opciones actualizadas correctamente");
        } catch (RSException e) {
            textZone.setInvalid(true);
            textZone.setErrorMessage(e.getMessage());
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        textZone.setValue(srvOptions.getZone());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {

    }
}
