package ru.owen.app.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;  // Use HSSFWorkbook for .xls files
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.model.Mutual.Modification;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class XlsToProductListParser {
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
        try (InputStream inputStream = new URL(ProjectConstants.OWEN_MODIFICATIONS_SRC).openStream()) {
            Workbook workbook = new HSSFWorkbook(inputStream);  // Use HSSFWorkbook for .xls files
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Cell cell = rowIterator.next().getCell(0);
                if (cell != null && cell.toString().equals("Текущая дата")) {
                    break;
                }
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(cell.toString());
                }
                data.put(row.getRowNum(), rowData);
            }
        }
        return data;
    }

    private static List<Modification> parseToProducts(Map<Integer, List<String>> data) {
        return data.values()
                .stream()
                .filter(p -> p.get(1) != null && !p.get(1).isEmpty())
                .map(productData -> {
                    Modification modification = new Modification();
                    modification.setPartNumber(productData.get(1));
                    modification.setWorkingTitle(productData.get(2));
                    modification.setModification(productData.get(3));
                    modification.setFullTitle(productData.get(4));
                    modification.setPrice(setNullIfEmptyString(XlsToProductListParser::parsePrice, productData.get(5)));
                    modification.setPriceNDS(setNullIfEmptyString(XlsToProductListParser::parsePrice, productData.get(6)));
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
            if (str.isEmpty()) {
                return null;
            } else {
                return function.apply(str);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
