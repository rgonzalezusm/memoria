package cl.rgonzalez.memoria.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Arrays;

public final class RSFrontendUtils {

    private RSFrontendUtils() {
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
}
