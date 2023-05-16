package cl.rgonzalez.memoria.ui.views.products;

import cl.rgonzalez.memoria.core.RSCsvProductIO;
import cl.rgonzalez.memoria.exceptions.RSException;
import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;
import cl.rgonzalez.memoria.ui.RSFrontendUtils;
import cl.rgonzalez.memoria.ui.views.RSMainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.io.InputStream;
import java.util.List;


@PageTitle("Productos > Importar")
@Route(value = "productos/importar", layout = RSMainLayout.class)
@PermitAll
public class RSViewProductsImport extends VerticalLayout {

    private RSSrvProduct srvProduct;
    //
    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload singleFileUpload = new Upload(memoryBuffer);
    private Label labelInfo = new Label("");
    private Button buttonOk = new Button("Importar");
    private Button buttonClear = new Button("Limpiar");
    private Button buttonBack = new Button("Volver");
    //
    private RSCsvProductIO.Data data = null;

    public RSViewProductsImport(RSSrvProduct srvProduct) {
        this.srvProduct = srvProduct;

        addClassName("default-view");
        labelInfo.setText("");

        add(new HorizontalLayout(singleFileUpload) {
            {
                getStyle().set("padding-top", "40px");
            }
        });
        add(new HorizontalLayout(labelInfo) {
            {
                getStyle().set("margin-bottom", "20px");
            }
        });
        add(new HorizontalLayout(buttonOk, buttonClear, buttonBack));

        singleFileUpload.setWidth("500px");

        singleFileUpload.addSucceededListener(event -> {
            InputStream fileData = memoryBuffer.getInputStream(); // Get information about the uploaded file
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();
            processFile(fileData, fileName, contentLength, mimeType);
        });

        this.buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.buttonOk.addClickListener(e -> importProductsActino());
        this.buttonClear.addClickListener(e -> clear());
        this.buttonBack.addClickListener(e -> UI.getCurrent().navigate("productos"));
    }

    private void processFile(InputStream is, String fileName, long contentLength, String mimeType) {
        try {
            RSCsvProductIO importer = new RSCsvProductIO();
            data = importer.importFile(is, srvProduct);
            labelInfo.setText("Nuevos: " + data.getProducts().size() + "; Replicados: " + data.getExists() + "; Omitidos: " + data.getSkipped());
        } catch (RSException e) {
            RSFrontendUtils.showError("Error al leer archivo .csv");
        }
    }

    private void importProductsActino() {
        if (data == null) {
            RSFrontendUtils.showWarn("Seleccione un archivo");
        }

        List<RSProduct> products = data.getProducts();
        if (!products.isEmpty()) {
            srvProduct.saveAll(products);
            clear();
            if (products.size() > 1) {
                RSFrontendUtils.showInfo("Se han agrega " + products.size() + " productos correctamente");
            } else {
                RSFrontendUtils.showInfo("Se ha agrega un producto correctamente");
            }
            UI.getCurrent().navigate("productos");
        } else {
            clear();
            RSFrontendUtils.showInfo("Sin productos a agregar");
        }
    }

    private void clear() {
        this.data = null;
        this.labelInfo.setText("");
        this.singleFileUpload.clearFileList();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

}
