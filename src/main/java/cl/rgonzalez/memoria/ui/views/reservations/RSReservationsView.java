package cl.rgonzalez.memoria.ui.views.reservations;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.RSDay;
import cl.rgonzalez.memoria.core.dto.RSDtoReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityReservation;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Reservas")
@Route(value = "reservas", layout = RSMainLayout.class)
@RolesAllowed("ADMIN")
public class RSReservationsView extends VerticalLayout {

    private RSSrvRoom srvRoom;
    private RSSrvReservation srvReservation;
    //
    private Grid<Row> grid;
    private ComboBox<RSEntityRoom> comboRoom = new ComboBox<>("Sala");
    private ComboBox<Integer> comboYear = new ComboBox<>("Año");
    private ComboBox<Integer> comboSemester = new ComboBox<>("Semestre");
    //
    private List<RSEntityRoom> rooms;

    public RSReservationsView(RSSrvRoom srvRoom, RSSrvReservation srvReservation) {
        this.srvRoom = srvRoom;
        this.srvReservation = srvReservation;
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
        comboRoom.setItemLabelGenerator(RSEntityRoom::getName);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.add(comboRoom);
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
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.setWidth("1300px");
        grid.setHeight("70vh");
        grid.addColumn(e -> e.getBlock().formatRange()).setHeader("Hora").setWidth("140px");
        grid.addColumn(e -> e.getClassId()).setHeader("Clase").setWidth("70px");

        for (RSDay day : RSDay.values()) {
            grid.addColumn(e -> e.getMap().get(day).getUser().getName()).setHeader(day.getName()).setWidth("180px");
        }

        return grid;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Row {
        private RSBlock block;
        private Integer classId;
        private Map<RSDay, RSEntityReservation> map = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        rooms = srvRoom.findAll();

        comboRoom.setItems(rooms);
        comboRoom.setValue(!rooms.isEmpty() ? rooms.get(0) : null);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        comboYear.setItems(RSFrontUtils.createYears());
        comboYear.setValue(year);

        int semester = RSFrontUtils.findSemester();
        comboSemester.setItems(new Integer[]{1, 2});
        comboSemester.setValue(semester);

        if (!rooms.isEmpty()) {
            srvReservation.findAll(rooms.get(0), year, semester, now);
        }

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
