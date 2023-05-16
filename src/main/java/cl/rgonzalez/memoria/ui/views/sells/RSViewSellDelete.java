package cl.rgonzalez.memoria.ui.views.sells;

import cl.rgonzalez.memoria.core.entity.RSSell;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.service.RSSrvSell;
import cl.rgonzalez.memoria.core.service.RSSrvUser;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;

@PageTitle("Ventas > Eliminar")
@Route(value = "ventas/eliminar", layout = RSMainLayout.class)
@PermitAll
public class RSViewSellDelete extends VerticalLayout {

    private RSSrvSell srvSell;
    private RSSrvUser srvUser;
    //
    private DateTimePicker datePicker = new DateTimePicker("Fecha");
    private TextField textUser = new TextField("Usuario");
    private TextField textPayStyle = new TextField("Forma de Pago");
    private IntegerField textTotal = new IntegerField("Total");
    private Button buttonDelete = new Button("Eliminar");
    private Button buttonBack = new Button("Volver");
    //
    private Binder<RSSell> binder = new Binder(RSSell.class);
    private RSSell sell = null;
    private RSUser user = null;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public RSViewSellDelete(RSSrvSell srvSell, RSSrvUser srvUser) {
        this.srvSell = srvSell;
        this.srvUser = srvUser;
        addClassName("default-view");

        FormLayout form = new FormLayout();
        form.add(datePicker);
        form.add(textUser);
        form.add(textPayStyle);
        form.add(textTotal);
        add(form);
        add(new HorizontalLayout(buttonDelete, buttonBack));

        form.setWidth("300px");

        binder.forField(datePicker).bind("dateTime");
        binder.forField(textUser).bind(e -> e.getUser() != null ? e.getUser().getUsername() : "", (e, s) -> {
        });
        binder.forField(textPayStyle).bind(e -> e.getPayStyle().getName(), (e, s) -> {
        });
        binder.forField(textTotal).bind("total");

        buttonDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonDelete.addClickListener(e -> deleteAction());
        buttonBack.addClickListener(e -> UI.getCurrent().navigate("ventas"));

        datePicker.setReadOnly(true);
        textUser.setReadOnly(true);
        textPayStyle.setReadOnly(true);
        textTotal.setReadOnly(true);
    }

    private void deleteAction() {
        if (sell == null) {
            return;
        }

        ConfirmDialog dialog = RSFrontendUtils.createConfirmDeleteDialog("La informacion relacionada con la venta no podra ser recuperada. Desea continuar?");
        dialog.addConfirmListener(e -> {
            srvSell.delete(sell);
            this.sell = null;
            RSFrontendUtils.showInfo("Venta eliminada");
            UI.getCurrent().navigate("ventas");
        });
        dialog.open();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        sell = ComponentUtil.getData(UI.getCurrent(), RSSell.class);
        if (sell != null) {
            binder.readBean(sell);
        }

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
