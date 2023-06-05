package cl.rgonzalez.memoria.ui.views.reservation;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.dto.RSDtoReservationEventualRow;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.entity.RSEntityUser;
import cl.rgonzalez.memoria.core.service.RSSrvReservation;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.security.RSAuthenticatedUser;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@PageTitle("Reserva Eventual")
@Route(value = "reserva_eventual", layout = RSMainLayout.class)
@PermitAll
public class RSReservationEventualView extends VerticalLayout {

    private RSAuthenticatedUser srvAuth;
    private RSSrvRoom srvRoom;
    private RSSrvReservation srvReservation;
    //
    private ComboBox<RSEntityRoom> comboRoom = new ComboBox<>("Sala");
    private DatePicker datePicker = new DatePicker("Dia");
    private Button buttonOk = new Button("Reservar");
    private Button buttonRoomDescription = new Button(VaadinIcon.FILE_TEXT.create());
    //
    private List<RSEntityRoom> rooms;
    private List<RSDtoReservationEventualRow> rows;

    public RSReservationEventualView(RSAuthenticatedUser srvAuth, RSSrvRoom srvRoom, RSSrvReservation srvReservation) {
        this.srvAuth = srvAuth;
        this.srvRoom = srvRoom;
        this.srvReservation = srvReservation;
        addClassName("default-view");

        add(createReservationForm());
        add(createBlocksGrid());
        add(buttonOk);

        buttonOk.addClickListener(e -> reserveAction());
        buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonRoomDescription.setTooltipText("Ver detalles de la sala");
        buttonRoomDescription.addClickListener(e -> RSFrontUtils.showRoomDescriptionAction(comboRoom.getValue()));
    }

    private Component createReservationForm() {
        comboRoom.setItemLabelGenerator(RSEntityRoom::getName);
        datePicker.setI18n(RSFrontUtils.createDatePickerI18n());
        datePicker.setValue(LocalDate.now());

        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.add(comboRoom);
        hl.add(datePicker);

        comboRoom.setWidth("150px");

        return hl;
    }

    private Component createBlocksGrid() {
        Grid<RSDtoReservationEventualRow> grid = new Grid<>();
        grid.setWidth("400px");
        grid.setHeight("660px");
        grid.addColumn(e -> e.getBlock().formatRange()).setHeader("Hora").setWidth("120px");
        grid.addColumn(e -> e.getBlock().getValue()).setHeader("Clase").setWidth("50px");
        grid.addColumn(new ComponentRenderer<>(
                row -> {
                    Boolean reserved = row.getReserved();
                    Checkbox checkbox = new Checkbox();
                    checkbox.setValue(reserved);
                    checkbox.addValueChangeListener(e -> {
                        row.setReserved(e.getValue());
                    });
                    return checkbox;
                }
        )).setHeader("").setWidth("50px");

        rows = new ArrayList<>();
        for (RSBlock block : RSBlock.values()) {
            rows.add(new RSDtoReservationEventualRow(block, false));
        }
        grid.setItems(rows);

        return grid;
    }

    private void reserveAction() {
        Optional<RSEntityUser> opt = srvAuth.get();
        if (opt.isEmpty()) {
            RSFrontUtils.showWarn("Usuario no encontrado. Reingree a la aplicacion");
            return;
        }

        RSEntityUser user = opt.get();
        RSEntityRoom room = comboRoom.getValue();
        ZonedDateTime znow = ZonedDateTime.now();
        LocalDate reservationDate = datePicker.getValue();

        List<RSBlock> blocks = new ArrayList<>();
        for (RSDtoReservationEventualRow row : rows) {
            Boolean reserved = row.getReserved();
            if (reserved) {
                blocks.add(row.getBlock());
            }
        }

        srvReservation.saveEventual(znow, user, room, reservationDate, blocks);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        rooms = srvRoom.findAll();
        comboRoom.setItems(rooms);
        comboRoom.setValue(!rooms.isEmpty() ? rooms.get(0) : null);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
