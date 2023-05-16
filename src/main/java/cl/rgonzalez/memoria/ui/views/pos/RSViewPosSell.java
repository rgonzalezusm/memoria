package cl.rgonzalez.memoria.ui.views.pos;

import cl.rgonzalez.memoria.core.RSPayStyle;
import cl.rgonzalez.memoria.core.entity.RSUser;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import cl.rgonzalez.memoria.core.service.RSSrvSell;

import java.util.Optional;

@PageTitle("Punto de Venta > Finalizar Venta")
@Route(value = "pos/venta", layout = RSMainLayout.class)
@PermitAll
public class RSViewPosSell extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvSell srvSell;
    private RSAuthenticatedUser authenticatedUser;
    //
    private Label labelTotalKey = new Label("Total ");
    private Label labelTotalValue = new Label();
    private Label labelPayStyleKey = new Label("Forma de Pago ");
    private RadioButtonGroup<RSPayStyle> radioGroupPayStyle = new RadioButtonGroup<>();
    private Label labelPayKey = new Label("Pago ");
    private IntegerField textPayValue = new IntegerField("");
    private Button buttonDefaultPay = new Button(VaadinIcon.CASH.create());
    private Label labelVueltoKey = new Label("Vuelto ");
    private Label labelVueltoValue = new Label("");
    private Button buttonSell = new Button("Finalizar Venta");
    private Button buttonBack = new Button("Volver");
    //
    private RSBeanSellContainer container = null;
    private double total = 0;

    public RSViewPosSell(RSSrvProduct srvProduct, RSSrvSell srvSell, RSAuthenticatedUser authenticatedUser) {
        this.srvProduct = srvProduct;
        this.srvSell = srvSell;
        this.authenticatedUser = authenticatedUser;
        addClassName("default-view");

        radioGroupPayStyle.setLabel("");
        radioGroupPayStyle.setItems(RSPayStyle.values());
        radioGroupPayStyle.setItemLabelGenerator(e -> e.getName());
        radioGroupPayStyle.setValue(RSPayStyle.EFECTIVO);

        add(new HorizontalLayout(labelTotalKey, labelTotalValue));
        add(new HorizontalLayout(labelPayStyleKey, radioGroupPayStyle));
        add(new HorizontalLayout(labelPayKey, new Label("$"), textPayValue, buttonDefaultPay));
        add(new HorizontalLayout(labelVueltoKey, new Label("$"), labelVueltoValue));
        add(new HorizontalLayout(buttonSell, buttonBack));

        textPayValue.setWidth("150px");
        textPayValue.focus();
        textPayValue.setAutoselect(true);
        textPayValue.setValueChangeMode(ValueChangeMode.EAGER);
        textPayValue.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        textPayValue.addValueChangeListener(e -> {
            Integer pay = e.getValue();
            if (pay != null) {
                double vuelto = pay - total;
                if (vuelto >= 0) {
                    String strVuelto = Integer.toString((int) vuelto);
                    this.labelVueltoValue.setText(strVuelto);
                } else {
                    this.labelVueltoValue.setText("Pago no válido");
                }
            } else {
                this.labelVueltoValue.setText("Pago no válido");
            }
        });

        buttonSell.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSell.addClickShortcut(Key.INSERT);
        buttonSell.setTooltipText("Presione INSERT para Finalizar Venta");
        buttonSell.addClickListener(e -> createSell());

        buttonBack.addClickListener(e -> UI.getCurrent().navigate("ventas"));

        buttonDefaultPay.setTooltipText("Mismo valor que el total");
        buttonDefaultPay.addClickListener(e -> {
            textPayValue.setValue((int) total);
        });

        this.labelTotalKey.getStyle().set("font-weight", "bold");
        this.labelPayStyleKey.getStyle().set("font-weight", "bold");
        this.labelPayKey.getStyle().set("font-weight", "bold");
        this.labelVueltoKey.getStyle().set("font-weight", "bold");

        this.labelTotalKey.setWidth("150px");
        this.labelPayStyleKey.setWidth("150px");
        this.labelPayKey.setWidth("150px");
        this.labelVueltoKey.setWidth("150px");
    }

    private void createSell() {
        Optional<RSUser> opt = authenticatedUser.get();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Problema de autentificacion. Por favor, identifiquese nuevamente");
            return;
        }

        Integer pay = textPayValue.getValue();
        if (pay != null) {
            double vuelto = pay - total;
            if (vuelto >= 0) {
                RSPayStyle payStyle = radioGroupPayStyle.getValue();
                srvSell.createSell(container.getUnits(), payStyle, total, opt.get());
                RSFrontendUtils.showInfo("Venta realizada");
                UI.getCurrent().navigate("pos");
            } else {
                RSFrontendUtils.showWarn("Pago no válido");
            }
        } else {
            RSFrontendUtils.showWarn("Pago no válido");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        container = ComponentUtil.getData(UI.getCurrent(), RSBeanSellContainer.class);
        if (container != null) {
            total = container.getUnits().stream().mapToDouble(e -> e.getAmount() * e.getPricePerUnit()).sum();
            String strTotal = Integer.toString((int) total);

            this.labelTotalValue.setText(strTotal);
            this.textPayValue.setValue((int) total);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

}
