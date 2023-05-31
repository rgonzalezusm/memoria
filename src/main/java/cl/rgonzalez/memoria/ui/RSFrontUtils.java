package cl.rgonzalez.memoria.ui;

import cl.rgonzalez.memoria.core.RSBlock;
import cl.rgonzalez.memoria.core.dto.RSDtoReservation;
import cl.rgonzalez.memoria.core.dto.RSDtoReservationSemestralRow;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RSFrontUtils {

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
        return month <= 6 ? 1 : 2;
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

}
