package cl.rgonzalez.memoria.ui;

import cl.rgonzalez.memoria.core.RSDayOfWeek;
import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class RSFrontUtils {

    public static final int[] SEMESTER1 = new int[]{3, 7};
    public static final int[] SEMESTER2 = new int[]{8, 12};
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss Z");
    public static final DateTimeFormatter DTF2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private RSFrontUtils() {
    }

    public static void showInfo(String msg) {
        Notification notification = Notification.show(msg, 1000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }

    public static void showWarn(String msg) {
        Notification notification = Notification.show(msg, 1000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    public static void showError(String msg) {
        Notification notification = Notification.show(msg, 1000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static void showFatal(String msg) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);

        Div text = new Div(new Text(msg));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> {
            notification.close();
        });

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        notification.open();
    }

    public static ConfirmDialog createConfirmDeleteDialog(String msg) {
        return createConfirmDialog("Confirmar Eliminacion", msg, "Eliminar");
    }

    public static ConfirmDialog createConfirmDialog(String title, String msg, String action) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(msg);
        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.setConfirmText(action);
        return dialog;
    }

    public static DatePicker.DatePickerI18n createDatePickerI18n() {
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
//        i18n.setWeek("Woche");
//        i18n.setCalendar("Kalender");
//        i18n.setClear("Löschen");
        i18n.setToday("Hoy");
        i18n.setCancel("Cancelar");
        i18n.setFirstDayOfWeek(1);
        i18n.setMonthNames(Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
        i18n.setWeekdays(Arrays.asList("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"));
        i18n.setWeekdaysShort(Arrays.asList("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"));
        return i18n;
    }

    public static HorizontalLayout hl(Component... comps) {
        HorizontalLayout hl = new HorizontalLayout(comps);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return hl;
    }

    public static List<Integer> createYears() {
        int year = LocalDate.now().getYear() + 2;

        List<Integer> years = new ArrayList<>();
        for (int i = year; i > year - 5; i--) {
            years.add(i);
        }
        return years;
    }

    public static int findSemester() {
        return findSemester(LocalDate.now());
    }

    public static int findSemester(LocalDate date) {
        int month = date.getMonthValue();
        return SEMESTER1[0] <= month && month <= SEMESTER1[1] ? 1 : 2;
    }

    public static void showRoomDescriptionAction(RSEntityRoom value) {
        Dialog dialog = new Dialog();

        FormLayout form = new FormLayout();
        form.setWidth("500px");
        form.addFormItem(new Label(value.getName()), "Nombre");
        form.addFormItem(new Label(Integer.toString(value.getCapacity())), "Capacidad");
        form.addFormItem(new Label(value.getDescription()), "Descripcion");
        dialog.add(form);
        dialog.open();
    }

    public static String format(ZonedDateTime zdate) {
        return zdate.format(DTF);
    }

    public static String format(ZonedDateTime zdate, ZoneId zone) {
        return zdate.withZoneSameInstant(zone).format(DTF2);
    }

    public static String formatDayOfWeek(int dow) {
        RSDayOfWeek dayOfWeek = RSDayOfWeek.getById(dow).orElseThrow();
        return dayOfWeek.getName();
    }

    public static String formatDayOfWeeek(Integer dayOfWeek) {
        if (dayOfWeek == null) {
            return "";
        }

        RSDayOfWeek day = RSDayOfWeek.getById(dayOfWeek).orElseThrow();
        return day.getName();
    }

//    public static LocalDate[] getSemesterRange(Integer year, Integer semester) {
//        int init = semester == 1 ? SEMESTER1[0] : SEMESTER2[1];
//        int size = semester == 1 ? SEMESTER1.length : SEMESTER2.length;
//        LocalDate from = LocalDate.of(year, init, 1);
//        LocalDate to = from.plusMonths(size);
//        return new LocalDate[]{from, to};
//    }
}
