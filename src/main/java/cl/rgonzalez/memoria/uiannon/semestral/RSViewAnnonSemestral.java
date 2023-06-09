package cl.rgonzalez.memoria.uiannon.semestral;

import cl.rgonzalez.memoria.RSFrontUtils;
import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.dto.RSDtoBlockAndDay;
import cl.rgonzalez.memoria.core.dto.RSDtoReservationSemestralRow;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.service.RSSrvReservation;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.uiannon.RSMainLayoutAnnon;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@PageTitle("Inicio")
@Route(value = "semestral", layout = RSMainLayoutAnnon.class)
@AnonymousAllowed
public class RSViewAnnonSemestral extends Div {

    private RSSrvRoom srvRoom;
    private RSSrvReservation srvReservation;
    private List<RSDtoReservationSemestralRow> rows = new ArrayList<>();
    ;
    //
    private TextField textName = new TextField();
    private TextField textCourse = new TextField();
    private IntegerField textParallel = new IntegerField();
    //
    private ComboBox<RSEntityRoom> comboRoom = new ComboBox<>();
    private ComboBox<Integer> comboYear = new ComboBox<>();
    private ComboBox<Integer> comboSemester = new ComboBox<>();
    //
    private Grid<RSDtoReservationSemestralRow> grid = new Grid<>();
    //
    private Button buttonOk = new Button("Reservar");
    private Button buttonRoomDescription = new Button(VaadinIcon.FILE_TEXT.create());

    //
    private List<RSEntityRoom> rooms;

    public RSViewAnnonSemestral(RSSrvRoom srvRoom, RSSrvReservation srvReservation) {
        this.srvRoom = srvRoom;
        this.srvReservation = srvReservation;
        addClassName("annon-view");

        add(new H3("Reserva Semestral"));
        add(createReservationForm());
        add(buttonOk);

        buttonOk.addClickListener(e -> reserveAction());
        buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonRoomDescription.setTooltipText("Ver detalles de la sala");
        buttonRoomDescription.addClickListener(e -> RSFrontUtils.showRoomDescriptionAction(comboRoom.getValue()));
    }

    private Component createReservationForm() {
        VerticalLayout vl = new VerticalLayout();

        comboYear.setItems(RSFrontUtils.createYears());
        comboSemester.setItems(1, 2);
        comboRoom.setItemLabelGenerator(RSEntityRoom::getName);
        comboYear.setValue(LocalDate.now().getYear());
        comboSemester.setValue(RSFrontUtils.findSemester());

        textName.setWidth("400px");
        textCourse.setWidth("250px");
        textParallel.setWidth("100px");
        comboRoom.setWidth("150px");
        comboYear.setWidth("150px");
        comboSemester.setWidth("150px");

        FormLayout fl = new FormLayout();
        fl.setWidth("500px");
        fl.addFormItem(textName, "Nombre");
        fl.addFormItem(textCourse, "Ramo");
        fl.addFormItem(textParallel, "Paralelo");
        fl.addFormItem(comboRoom, "Sala");
        fl.addFormItem(comboYear, "Año");
        fl.addFormItem(comboSemester, "Semestre");

        vl.add(fl);
        vl.add(createBlocksGrid());

        return vl;
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
                                if (reserved) {
                                    return new Label("");
                                } else {
                                    Checkbox checkbox = new Checkbox();
                                    checkbox.addValueChangeListener(e -> {
                                        row.getMap().put(day, e.getValue());
                                    });
                                    return checkbox;
                                }
                            }
                    )
            ).setHeader(day.getName()).setWidth("60px");
        }

        return grid;
    }

    private void reserveAction() {
        String name = textName.getValue();
        if (name == null || name.isEmpty()) {
            RSFrontUtils.showWarn("Ingrese nombre");
            return;
        }

        String course = textCourse.getValue();
        if (course == null || course.isEmpty()) {
            RSFrontUtils.showWarn("Ingrese ramo");
            return;
        }

        Integer par = textParallel.getValue();
        if (par == null) {
            RSFrontUtils.showWarn("Ingrese paralelo");
            return;
        }

        ZonedDateTime znow = ZonedDateTime.now();
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

        srvReservation.saveSemestral(name, course, par, znow, room, year, semester, reservations);
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

        updateGrid();
    }

    private void updateGrid() {
        if (comboRoom.isEmpty()) {
            return;
        }

        Set<String> reservedSet = new HashSet<>();
        List<RSEntityReservation> reservations = srvReservation.findByRoomAndSemester(comboRoom.getValue(), comboYear.getValue(), comboSemester.getValue());
        for (RSEntityReservation r : reservations) {
            Integer dow = r.getSemesterDayOfWeek();
            Integer block = r.getBlock();
            reservedSet.add(block + "-" + dow);
        }

        rows = new ArrayList<>();
        for (RSBlock block : RSBlock.values()) {
            Map<RSDayOfWeek, Boolean> map = new HashMap<>();
            for (RSDayOfWeek dow : RSDayOfWeek.values()) {
                map.put(dow, reservedSet.contains(block.getValue() + "-" + dow));
            }
            rows.add(new RSDtoReservationSemestralRow(block, map));
        }
        grid.setItems(rows);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
