package cl.rgonzalez.memoria.ui.views.reservation;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.dto.RSDtoBlockAndDay;
import cl.rgonzalez.memoria.core.dto.RSDtoReservationSemestralRow;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
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

@PageTitle("Reserva Semestral")
@Route(value = "reserva_semestral", layout = RSMainLayout.class)
@PermitAll
public class RSReservationSemesterView extends VerticalLayout {

    private RSAuthenticatedUser srvAuth;
    private RSSrvRoom srvRoom;
    private RSSrvReservation srvReservation;
    private List<RSDtoReservationSemestralRow> rows;
    //
    private ComboBox<RSEntityRoom> comboRoom = new ComboBox<>("Sala");
    private ComboBox<Integer> comboYear = new ComboBox<>("Año");
    private ComboBox<Integer> comboSemester = new ComboBox<>("Semestre");
    private Button buttonOk = new Button("Reservar");
    private Button buttonRoomDescription = new Button(VaadinIcon.FILE_TEXT.create());
    private Grid<RSDtoReservationSemestralRow> grid = new Grid<>();
    //
    private List<RSEntityRoom> rooms;

    public RSReservationSemesterView(RSAuthenticatedUser srvAuth, RSSrvRoom srvRoom, RSSrvReservation srvReservation) {
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
        comboYear.setItems(RSFrontUtils.createYears());
        comboSemester.setItems(1, 2);

        comboRoom.setItemLabelGenerator(RSEntityRoom::getName);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.add(comboRoom);
        hl.add(comboYear);
        hl.add(comboSemester);

        comboRoom.setWidth("150px");
        comboYear.setWidth("150px");
        comboSemester.setWidth("150px");

        comboYear.setValue(LocalDate.now().getYear());
        comboSemester.setValue(RSFrontUtils.findSemester());

        return hl;
    }

    private Component createBlocksGrid() {
        grid.setWidth("900px");
        grid.setHeight("660px");
        grid.addColumn(e -> e.getBlock().formatRange()).setHeader("Hora").setWidth("120px");
        grid.addColumn(e -> e.getBlock().getValue()).setHeader("Clase").setWidth("50px");
        for (RSDayOfWeek day : RSDayOfWeek.values()) {
            grid.addColumn(
                    new ComponentRenderer<>(
                            row -> {
                                Boolean reserved = row.getMap().get(day);
                                Checkbox checkbox = new Checkbox();
                                checkbox.setValue(reserved);
                                checkbox.addValueChangeListener(e -> {
                                    row.getMap().put(day, e.getValue());
                                });

                                return checkbox;
                            }
                    )
            ).setHeader(day.getName()).setWidth("60px");
        }

        clearRows();
        return grid;
    }

    private void reserveAction() {
        Optional<RSEntityUser> opt = srvAuth.get();
        if (opt.isEmpty()) {
            RSFrontUtils.showWarn("Usuario no encontrado. Reingree a la aplicacion");
            return;
        }

        ZonedDateTime znow = ZonedDateTime.now();
        RSEntityUser user = opt.get();
        RSEntityRoom room = comboRoom.getValue();
        int year = comboYear.getValue();
        Integer semester = comboSemester.getValue();

        List<RSDtoBlockAndDay> reservations = new ArrayList<>();
        for (RSDtoReservationSemestralRow row : rows) {
            for (RSDayOfWeek day : RSDayOfWeek.values()) {
                Boolean reserved = row.getMap().get(day);
                if (reserved) {
                    reservations.add(new RSDtoBlockAndDay(row.getBlock(), day));
                }
            }
        }

        if (reservations.isEmpty()) {
            RSFrontUtils.showWarn("Seleccione al menos un bloque");
            return;
        }

        List<RSEntityReservation> existingRes = srvReservation.findSemestralReservations(room, year, semester, reservations);
        if (!existingRes.isEmpty()) {
            RSFrontUtils.showWarn("Uno o mas bloques ya se encuentran reservados");
            return;
        }

        srvReservation.saveSemestral(znow, user, room, year, semester, reservations);
        RSFrontUtils.showInfo("Reserva realizada");
        clear();
    }

    private void clear() {
        clearRows();
    }

    private void clearRows() {
        rows = new ArrayList<>();
        for (RSBlock block : RSBlock.values()) {
            Map<RSDayOfWeek, Boolean> map = new HashMap<>();
            for (RSDayOfWeek day : RSDayOfWeek.values()) {
                map.put(day, false);
            }
            rows.add(new RSDtoReservationSemestralRow(block, map));
        }
        grid.setItems(rows);
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
