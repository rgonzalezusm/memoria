package cl.rgonzalez.memoria.junit;

import cl.rgonzalez.memoria.core.RSCsvProductIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JU_CsvImporter01 {

    @Test
    public void test() {
        RSCsvProductIO importer = new RSCsvProductIO();
        Assertions.assertEquals(1, importer.findIndex(new String[]{"", "codigo", "descripcion"}, RSCsvProductIO.CODIGO));
        Assertions.assertEquals(-1, importer.findIndex(new String[]{"", "code", "descripcion"}, RSCsvProductIO.CODIGO));
        Assertions.assertEquals(2, importer.findIndex(new String[]{"", "code", "descripcion"}, RSCsvProductIO.DESCRIPCION));
    }
}
