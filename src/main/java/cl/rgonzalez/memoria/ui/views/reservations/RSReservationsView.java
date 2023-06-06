package cl.rgonzalez.memoria.ui.views.reservations;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.RSReservationType;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.service.RSSrvOptions;
import cl.rgonzalez.memoria.core.service.RSSrvReservation;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import cl.rgonzalez.memoria.ui.RSFrontUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Reservas")
@Route(value = "reservas", layout = RSMainLayout.class)
@RolesAllowed("ADMIN")
public class RSReservationsView extends VerticalLayout {

    private RSSrvRoom srvRoom;
    private RSSrvReservation srvReservation;
    private RSSrvOptions srvOptions;
    //
    private Grid<RSEntityReservation> grid = new Grid<>();
    private ComboBox<Integer> comboYear = new ComboBox<>("Año");
    private ComboBox<Integer> comboSemester = new ComboBox<>("Semestre");
    //
    private List<RSEntityRoom> rooms;
    private ZoneId zone;

    public RSReservationsView(RSSrvRoom srvRoom, RSSrvReservation srvReservation, RSSrvOptions srvOptions) {
        this.srvRoom = srvRoom;
        this.srvReservation = srvReservation;
        this.srvOptions = srvOptions;
        addClassName("default-view");

        add(createButtonsArea());
        add(createGrid());

//        List<Row> rows = new ArrayList<>();
//        int i = 1;
//        for (RSBlock block : RSBlock.values()) {
//            Row row = new Row();
//            row.setBlock(block);
//            row.setClassId(i++);
//            for (RSDay day : RSDay.values()) {
//                row.getMap().put(day, new RSDtoReservation());
//            }
//            rows.add(row);
//        }
//        this.grid.setItems(rows);
    }

    private Component createButtonsArea() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.add(comboYear);
        hl.add(comboSemester);

        return hl;
    }

//    private Component createGridArea() {
//        HorizontalLayout hl = new HorizontalLayout();
//        hl.add(createGrid());
//        return hl;
//    }

    private Component createGrid() {
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setWidth("1400px");
        grid.setHeight("70vh");
        grid.addColumn(r -> RSFrontUtils.format(r.getReservationDate(), zone)).setHeader("Hora").setWidth("200px");
        grid.addColumn(r -> r.getUser().getName()).setHeader("Usuario").setWidth("120px");
        grid.addColumn(r -> RSReservationType.get(r.getType()).getName()).setHeader("Tipo").setWidth("120px");
        grid.addColumn(r -> r.getYear()).setHeader("Año").setWidth("80px");
        grid.addColumn(r -> r.getSemester()).setHeader("Semestre").setWidth("80px");
        grid.addColumn(r -> r.getRoom().getNumber()).setHeader("Sala").setWidth("80px");
        grid.addColumn(r -> r.getBlock()).setHeader("Bloque").setWidth("80px");
        grid.addColumn(r -> RSFrontUtils.formatDayOfWeeek(r.getSemesterDayOfWeek())).setHeader("Dia Semana").setWidth("80px");
        grid.addColumn(r -> r.getEventualMonth()).setHeader("Ev. Mes").setWidth("80px");
        grid.addColumn(r -> r.getEventualDay()).setHeader("Ev. Dia").setWidth("80px");
        return grid;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Row {
        private RSBlock block;
        private Integer classId;
        private Map<RSDayOfWeek, RSEntityReservation> map = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        rooms = srvRoom.findAll();

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        comboYear.setItems(RSFrontUtils.createYears());
        comboYear.setValue(year);

        int semester = RSFrontUtils.findSemester();
        comboSemester.setItems(1, 2);
        comboSemester.setValue(semester);

        this.zone = ZoneId.of(srvOptions.getZone());

        comboYear.addValueChangeListener(e -> updateGrid());
        comboSemester.addValueChangeListener(e -> updateGrid());

        if (!rooms.isEmpty()) {
            updateGrid();
        }
    }

    private void updateGrid() {
        Integer year = comboYear.getValue();
        Integer semester = comboSemester.getValue();

        List<RSEntityReservation> reservations = srvReservation.findBySemester(year, semester);
        grid.setItems(reservations);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
