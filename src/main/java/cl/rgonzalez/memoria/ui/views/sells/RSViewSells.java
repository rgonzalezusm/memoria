package cl.rgonzalez.memoria.ui.views.sells;

import cl.rgonzalez.memoria.core.entity.RSSell;
import cl.rgonzalez.memoria.core.service.RSSrvOptions;
import cl.rgonzalez.memoria.core.service.RSSrvSell;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@PageTitle("Ventas")
@Route(value = "ventas", layout = RSMainLayout.class)
@PermitAll
public class RSViewSells extends VerticalLayout {

    private RSSrvSell srvSell;
    private RSSrvOptions srvOptions;
    //
    private DatePicker datePicker = new DatePicker("Fecha");
    private Button buttonBack = new Button("<");
    private Button buttonNext = new Button(">");
    //
    private Grid<RSSell> grid;
    private GridListDataView<RSSell> gview;
    private Button buttonDelete = new Button("Eliminar");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public RSViewSells(RSSrvSell srvSell, RSSrvOptions srvOptions) {
        this.srvSell = srvSell;
        this.srvOptions = srvOptions;

        addClassName("default-view");
        add(createTopArea());
        add(createGridArea());

        datePicker.setLocale(Locale.of("es-CL"));
        datePicker.setI18n(RSFrontendUtils.createDatePickerI18n());
        datePicker.setValue(LocalDate.now());
        datePicker.addValueChangeListener(e -> updateGrid());
        buttonBack.addClickListener(e -> previousDay());
        buttonNext.addClickListener(e -> nextDay());

        buttonDelete.addClickListener(e -> deleteAction());
    }

    public Component createTopArea() {
        HorizontalLayout hl = new HorizontalLayout(datePicker, buttonBack, buttonNext);
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        return hl;
    }

    public Component createGridArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(createGrid());
        hl.add(createGridButtons());
        return hl;
    }

    public Component createGrid() {
        ZoneId z = ZoneId.of(srvOptions.getZone());

        grid = new Grid<>();
        grid.setWidth("900px");
        grid.setHeight("70vh");
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(e -> e.getDateTime().atZone(z).format(dtf)).setHeader("Fecha").setSortable(true);
        grid.addColumn(e -> e.getUser() != null ? e.getUser().getUsername() : "").setHeader("Usuario").setSortable(true);
        grid.addColumn(e -> e.getPayStyle().getName()).setHeader("Forma de Pago").setSortable(true);
        grid.addColumn(RSSell::getTotal).setHeader("Total").setSortable(true);
        return grid;
    }

    public Component createGridButtons() {
        Div div = new Div();
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        div.add(buttonDelete);
        return div;
    }

    private void updateGrid() {
        List<RSSell> sells = srvSell.findAllByDate(datePicker.getValue());
        gview = grid.setItems(sells);
    }

    private void previousDay() {
        this.datePicker.setValue(datePicker.getValue().minusDays(1));
        updateGrid();
    }

    private void nextDay() {
        this.datePicker.setValue(datePicker.getValue().plusDays(1));
        updateGrid();
    }

    private void deleteAction() {
        Optional<RSSell> opt = grid.getSelectionModel().getFirstSelectedItem();
        if (opt.isEmpty()) {
            RSFrontendUtils.showWarn("Seleccione una venta");
            return;
        }

        ComponentUtil.setData(UI.getCurrent(), RSSell.class, opt.get());
        UI.getCurrent().navigate("ventas/eliminar");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        List<RSSell> sells = srvSell.findAllByDate(LocalDate.now());
        gview = this.grid.setItems(sells);
//        this.labelTotal.setText("Total: " + sells.size());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
    }

}
