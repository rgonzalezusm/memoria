package cl.rgonzalez.memoria.core;

import cl.rgonzalez.memoria.exceptions.RSException;
import com.helger.commons.csv.CSVWriter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import cl.rgonzalez.memoria.core.entity.RSCategory;
import cl.rgonzalez.memoria.core.entity.RSProduct;
import cl.rgonzalez.memoria.core.service.RSSrvProduct;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RSCsvProductIO {

    public static final String CODIGO = "codigo";
    public static final String DESCRIPCION = "descripcion";
    public static final String PRECIO = "precio";

    public Data importFile(InputStream is, RSSrvProduct service) throws RSException {
        Map<String, RSProduct> map = new HashMap<>();
        int skipped = 0;
        int exists = 0;
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(is))) {
            String line = bf.readLine(); // CSV HEADER

            String[] split = line.split(";");
            int codeIndex = findIndex(split, CODIGO);
            int descIndex = findIndex(split, DESCRIPCION);
            int priceIndex = findIndex(split, PRECIO);
            int limit = split.length;

            RSUtils.verifyIOEx(codeIndex == -1, "columna '" + CODIGO + "' no encontrada");
            RSUtils.verifyIOEx(descIndex == -1, "columna '" + DESCRIPCION + "' no encontrada");
            RSUtils.verifyIOEx(priceIndex == -1, "columna '" + PRECIO + "' no encontrada");

            while ((line = bf.readLine()) != null) {
                split = line.split(";", limit);
                String code = split[codeIndex];
                String desc = split[descIndex];
                String priceStr = split[priceIndex];

                Integer price = RSUtils.parseInt(priceStr);

                if (code.isEmpty()) {
                    skipped++;
                    continue;
                }

                if (!service.findByCode(code).isEmpty()) {
                    exists++;
                    continue;
                }

                if (map.containsKey(code)) {
                    exists++;
                    continue;
                }

                if (price == null) {
//                    System.out.println(code + " " + desc + " " + price);
//                    skipped++;
//                    continue;
                    price = 0;
                }

                RSProduct p = new RSProduct();
                p.setCode(code);
                p.setDescription(desc);
                p.setSellPrice(price);
                map.put(code, p);
            }

            List<RSProduct> products = map.values().stream().collect(Collectors.toList());
            return new Data(products, skipped, exists);
        } catch (IOException ex) {
            throw new RSException("Error al leer archivo .csv", ex);
        }
    }

    public int findIndex(String[] headers, String header) {
        for (int i = 0; i < headers.length; i++) {
            String str = headers[i];
            if (str.equals(header)) {
                return i;
            }
        }
        return -1;
    }

    public StringWriter export(RSSrvProduct service) {
        StringWriter stringWriter = new StringWriter();

        CSVWriter csvWriter = new CSVWriter(stringWriter);
        csvWriter.setSeparatorChar(';');
        csvWriter.setApplyQuotesToAll(false);
        csvWriter.writeNext(CODIGO, DESCRIPCION, "categoria", PRECIO);

        for (RSProduct pr : service.findAll()) {
            RSCategory cat = pr.getCategory();
            Integer sellPrice = pr.getSellPrice();
            String strCat = cat != null ? cat.getName() : "";
            String strPrice = sellPrice != null ? Integer.toString(sellPrice) : "0";
            csvWriter.writeNext(pr.getCode(), pr.getDescription(), strCat, strPrice);
        }


        return stringWriter;
    }

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private List<RSProduct> products;
        private int skipped = 0;
        private int exists = 0;
    }
}
