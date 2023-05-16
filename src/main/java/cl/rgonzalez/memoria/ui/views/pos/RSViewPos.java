package cl.rgonzalez.memoria.ui.views.pos;

import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.entity.RSSellUnit;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import cl.rgonzalez.memoria.core.service.RSSrvSell;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Punto de Venta")
@Route(value = "pos", layout = RSMainLayout.class)
@RouteAlias(value = "", layout = RSMainLayout.class)
@PermitAll
public class RSViewPos extends VerticalLayout {

    private RSSrvProduct srvProduct;
    private RSSrvSell srvSell;
    //
    private TextField textCode = new TextField("Codigo");
    private Button buttonAddCode = new Button("Agregar");
    private Button buttonSell = new Button("Finalizar Venta");
    //
    private Grid<RSSellUnit> gridProducts;
    private GridListDataView<RSSellUnit> gview;
    private Button buttonEdit = new Button("Editar");
    private Button buttonDelete = new Button("Eliminar");
    //
    private Label labelTotal = new Label("Total: $0");
    private EditAmountDialog dialogEditAmount = new EditAmountDialog();
    private CreateSellDialog dialogCreateSell = new CreateSellDialog();

    public RSViewPos(RSSrvProduct srvProduct, RSSrvSell srvSell) {
        this.srvProduct = srvProduct;
        this.srvSell = srvSell;
        addClassName("default-view");

        add(createCodeArea());
        add(createGridArea());
        add(createPaymentArea());

        buttonAddCode.addClickListener(e -> addProduct());
        buttonAddCode.addClickShortcut(Key.ENTER);

        buttonDelete.addClickListener(e -> deleteProduct());
        buttonDelete.addClickShortcut(Key.DELETE);

        buttonEdit.addClickListener(e -> editProductAmount());

        buttonSell.addClickListener(e -> createSell());
//        buttonSell.addClickShortcut(Key.ENTER, KeyModifier.CONTROL);
        buttonSell.addClickShortcut(Key.INSERT);
        buttonSell.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSell.setTooltipText("Presione INSERT para Finalizar Venta");
    }

    private Component createCodeArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.add(textCode);
        hl.add(buttonAddCode);
        hl.add(buttonSell);

        this.textCode.setWidth("30rem");
        this.textCode.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        return hl;
    }

    private Component createGridArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(createGrid());
        hl.add(createGridButtons());
        return hl;
    }

    private Component createGrid() {
        gridProducts = new Grid<>();
        gridProducts.setId("PosView-gridProducts");
        gridProducts.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        gridProducts.setWidth("900px");
        gridProducts.addColumn(e -> e.getProduct().getCode()).setHeader("Codigo").setWidth("150px");
        gridProducts.addColumn(e -> e.getProduct().getDescription()).setHeader("Descripcion").setWidth("400px");
        gridProducts.addColumn(e -> e.getAmount()).setHeader("Cantidad").setWidth("100px");
        gridProducts.addColumn(e -> e.getPricePerUnit()).setHeader("Precio").setWidth("100px");
        gridProducts.addColumn(e -> (int) (e.getAmount() * e.getPricePerUnit())).setHeader("Subtotal").setWidth("100px");

        gview = gridProducts.setItems(new ArrayList<>());

        return gridProducts;
    }

    private Component createGridButtons() {
        Div hl = new Div();
        hl.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        hl.add(buttonEdit);
        hl.add(buttonDelete);
        return hl;
    }

    private Component createPaymentArea() {
        HorizontalLayout h1 = new HorizontalLayout(labelTotal);
        h1.setDefaultVerticalComponentAlignment(Alignment.END);
        this.labelTotal.getElement().getStyle().set("font-size", "35px");
        this.labelTotal.getElement().getStyle().set("font-weight", "bold");

//        this.textPay.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
//        HorizontalLayout h2 = new HorizontalLayout(textPay);
//        h2.setDefaultVerticalComponentAlignment(Alignment.END);

        Div div = new Div();
        div.setWidth("900px");
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.BETWEEN);
        div.add(h1);
        return div;
    }

    private void addProduct() {
        String code = this.textCode.getValue();
        if (code == null || code.isEmpty()) {
            this.textCode.clear();
            return;
        }

        Optional<RSProduct> opt = srvProduct.findByCode(code);
        if (opt.isEmpty()) {
            this.textCode.clear();
            return;
        }

        RSProduct product = opt.get();
        Optional<RSSellUnit> opt2 = gview.getItems().filter(e -> e.getProduct().getCode().equals(product.getCode())).findFirst();
        if (opt2.isEmpty()) {
            RSSellUnit unit = new RSSellUnit();
            unit.setProduct(product);
            unit.setPricePerUnit(product.getSellPrice());
            unit.setAmount(1.0);
            gview.addItem(unit).refreshAll();
        } else {
            Double amount = opt2.get().getAmount();
            opt2.get().setAmount(amount + 1.0);
            gview.refreshAll();
        }

        double total = gview.getItems().mapToDouble(u -> u.getAmount() * u.getPricePerUnit()).sum();
        this.labelTotal.setText("Total: $" + (int) total);
        this.textCode.clear();
    }

    private void deleteProduct() {
        Optional<RSSellUnit> opt = this.gridProducts.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            return;
        }

        RSSellUnit unit = opt.get();
        gview.removeItem(unit).refreshAll();

        double total = gview.getItems().mapToDouble(u -> u.getAmount() * u.getPricePerUnit()).sum();
        this.labelTotal.setText("Total: $" + (int) total);
//        this.textCode.clear();
    }

    private void editProductAmount() {
        Optional<RSSellUnit> opt = this.gridProducts.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            return;
        }

        RSSellUnit unit = opt.get();
        dialogEditAmount.setUnit(unit);
        dialogEditAmount.open();
    }

    private void createSell() {
        List<RSSellUnit> items = gview.getItems().collect(Collectors.toList());
        if (!items.isEmpty()) {
            ComponentUtil.setData(UI.getCurrent(), RSBeanSellContainer.class, new RSBeanSellContainer(items));
            UI.getCurrent().navigate("pos/venta");
        } else {
            RSFrontendUtils.showWarn("Ingrese productos");
        }
    }

    private class EditAmountDialog extends Dialog {

        private NumberField textAmount = new NumberField("Cantidad");
        private Button saveButton = new Button("Actualizar", e -> updateAction());
        private Button cancelButton = new Button("Cancelar", e -> close());
        private RSSellUnit unit = null;

        public EditAmountDialog() {
            setHeaderTitle("Editar Cantidad");
            add(new VerticalLayout(labelTotal));
            add(new VerticalLayout(textAmount));
            getFooter().add(cancelButton, saveButton);
            saveButton.addClickShortcut(Key.ENTER);
        }

        private void updateAction() {
            Double value = textAmount.getValue();
            if (value != null && value > 0) {
                unit.setAmount(value);
                gview.refreshAll();

                double total = gview.getItems().mapToDouble(u -> u.getAmount() * u.getPricePerUnit()).sum();
                labelTotal.setText("Total: $" + (int) total);
                close();
            }
        }

        public void setUnit(RSSellUnit unit) {
            this.unit = unit;
        }
    }

    private class CreateSellDialog extends Dialog {

        private Label labelTotal = new Label("Total: 0");
        private NumberField textPay = new NumberField("Pago");
        private Label labelVuelto = new Label("Vuelto: ");
        private TextField textVuelto = new TextField("$0");
        private Button saveButton = new Button("Aceptar", e -> okAction());
        private Button cancelButton = new Button("Cancelar", e -> close());
        //
        private double total = 0;
        private double pay = 0;

        public CreateSellDialog() {
            setHeaderTitle("Generar Venta");
            add(new HorizontalLayout(labelTotal));
            add(new HorizontalLayout(textPay));
            add(new HorizontalLayout(labelVuelto, textVuelto));
            getFooter().add(cancelButton, saveButton);

            textVuelto.setReadOnly(true);

            textPay.addValueChangeListener(e -> {
                Double pay = textPay.getValue();
                if (pay != null) {
                    int vuelto = (int) (pay - total);
                    String vueltoStr = vuelto < 0 ? "Pago no válido" : "$" + Integer.toString(vuelto);

                    System.out.println(pay + " " + total + " " + vuelto);
                    textVuelto.setValue(vueltoStr);
                } else {
                    textVuelto.setValue("Pago no válido");
                }
            });

//            this.saveButton.addClickShortcut(Key.ENTER);
            this.textPay.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        }

        private void updateForm(double total, double pay) {
            this.total = total;
            this.pay = total;
            int vuelto = (int) (pay - total);
            String vueltoStr = vuelto < 0 ? "Pago no válido" : Integer.toString(vuelto);

            labelTotal.setText("Total: $" + total);
            textPay.focus();
            textPay.setValue(pay);
            textVuelto.setValue("$" + vueltoStr);
        }

        private void okAction() {
            int vuelto = (int) (pay - total);
            if (vuelto >= 0) {
//                List<TPSellUnit> units = gview.getItems().collect(Collectors.toList());
//                srvSell.createSell(units, total);
                close();
            }
        }

        public Label getLabelTotal() {
            return labelTotal;
        }

        public NumberField getTextPay() {
            return textPay;
        }

        public Label getLabelVuelto() {
            return labelVuelto;
        }
    }

}
