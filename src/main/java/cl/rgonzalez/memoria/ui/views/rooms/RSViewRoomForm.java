package cl.rgonzalez.memoria.ui.views.rooms;

import cl.rgonzalez.memoria.core.entity.RSEntityRoom;
import cl.rgonzalez.memoria.core.service.RSSrvRoom;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class RSViewRoomForm extends FormLayout {

    private RSSrvRoom srvRoom;
    //
    private IntegerField textNumber = new IntegerField("Numero");
    private TextField textName = new TextField("Nombre");
    private IntegerField textCapacity = new IntegerField("Capacidad");
    private TextArea textareaDescription = new TextArea("Description");
    //
    private RSEntityRoom user = new RSEntityRoom();
    private Binder<RSEntityRoom> binder = new Binder(RSEntityRoom.class);

    public RSViewRoomForm(RSSrvRoom srvRoom) {
        this.srvRoom = srvRoom;

        add(textNumber);
        add(textName);
        add(textCapacity);
        add(textareaDescription);

        binder.forField(textNumber)
                .withValidator(val -> val != null, "Numero nulo")
                .bind("number");
        binder.forField(textName)
                .bind("name");
        binder.forField(textCapacity)
                .withValidator(name -> name != null, "Capacidad nula")
                .bind("capacity");
        binder.forField(textareaDescription)
                .bind("description");
    }

    public void setUser(RSEntityRoom room) {
        this.user = room;
        this.binder.readBean(room);
    }

    public Binder<RSEntityRoom> getBinder() {
        return binder;
    }

    public IntegerField getTextNumber() {
        return textNumber;
    }

    public TextField getTextName() {
        return textName;
    }

    public IntegerField getTextCapacity() {
        return textCapacity;
    }

    public TextArea getTextareaDescription() {
        return textareaDescription;
    }
}
