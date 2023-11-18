package ru.owen.app.util;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.model.Modification;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CsvToProductListParser {
    private static final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

    public List<Modification> getProducts() {
        try {
            return parseToProducts(getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Integer, List<String>> getData() throws IOException {
        Map<Integer, List<String>> data = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new URL(ProjectConstants.OWEN_MODIFICATIONS_SRC).openStream()))) {
            String[] nextLine;
            int row = 0;

            while (true) {
                try {
                    if ((nextLine = csvReader.readNext()) == null) break;
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                List<String> rowData = new ArrayList<>(Arrays.asList(nextLine));
                data.put(row, rowData);
                row++;
            }
        }
        return data;
    }

    private static List<Modification> parseToProducts(Map<Integer, List<String>> data) {
        data.remove(0);
        return data.values()
                .stream()
                .map(productData -> {

                    Modification modification = new Modification();
                    modification.setPartNumber(productData.get(1));
                    modification.setWorkingTitle(productData.get(2));
                    modification.setModification(productData.get(3));
                    modification.setFullTitle(productData.get(4));
                    modification.setPrice_(setNullIfEmptyString(CsvToProductListParser::parsePrice, productData.get(5)));
                    modification.setPriceNDS(setNullIfEmptyString(CsvToProductListParser::parsePrice, productData.get(6)));
                    modification.setProductSerial(productData.get(7));
                    modification.setGroup(productData.get(8));
                    modification.setDeliveryTime(productData.get(9));
                    modification.setSize(setNullIfEmptyString(Integer::parseInt, productData.get(10)));
                    modification.setOversize(setNullIfEmptyString(Integer::parseInt, productData.get(11)));
                    modification.setMultiplicity(setNullIfEmptyString(Integer::parseInt, productData.get(12)));
                    modification.setCodeTNVED(productData.get(13));
                    modification.setStatus(productData.get(14));
                    modification.setGuaranteePeriod(setNullIfEmptyString(Short::parseShort, productData.get(15)));

                    modification.setMarketExitDate(productData.get(16));
                    return modification;
                })
                .collect(Collectors.toList());
    }

    private static Double parsePrice(String value) {
        try {
            return format.parse(value).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static <R> R setNullIfEmptyString(Function<String, R> function, String str) {
        try {
            if (str.isEmpty()) return null;
            else return function.apply(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

