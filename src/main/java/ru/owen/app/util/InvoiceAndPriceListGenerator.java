package ru.owen.app.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class InvoiceAndPriceListGenerator {
//    @Value("${root}")
//    private String root;
    private static final String PDF = ".pdf";

    // Specify the Dart script file path
//    private static final String dartScriptPath = "/home/alexandrov/app/main.aot";
    private static final String dartScriptPath = "D:\\OWEN\\app\\src\\main\\resources\\main.aot";

    public String createInvoice(int customerId, String invoiceName, String companyData, String deliveryAddressValue, String deliveryPriceValue,
                                String itogoValue, String ndsValue, String couponValue, String tovaryListValue) {
        String pathValue = ".\\" + customerId + "\\";
        try {

            // Additional command-line arguments
            String subCommand = "invoice_gen";

            String pathArg = "--savePath";

            String invoiceNumberArg = "--invoiceNumber";
//            invoiceName = "12345678";

            String customerDataArg = "--customerData";
//            companyData = "ООО «Сценический портал», ИНН 7715503125, 124365, г. Москва, Зеленоград, ул. Заводская д.21А, стр.1";

            String deliveryAddressArg = "--deliveryAddress";
//            deliveryAddressValue = "Самовывоз г.Москва 1я ул.Энтузиастов д.4";

            String deliveryPriceArg = "--deliveryPrice";
//            String deliveryPriceValue = "0,00";

            String itogoArg = "--itogo";
//            itogoValue = "5 000,00";

            String ndsArg = "--nds";
//            ndsValue = "342,00";

            String couponArg = "--coupon";
//            couponValue = "-1%";
            couponValue = "-" + couponValue + "%";

            //[Num of a product in the list,fullTitle of modification,amount of a product,price,sum,delivery time]
            String tovaryListArg = "--tovaryList";
//            tovaryListValue = "[[Позиция1,Позиция2,Позиция3,Позиция4,Позиция5,Позиция6]," +
//                    "[Позиция1,Позиция2,Позиция3,Позиция4,Позиция5,Позиция6],[Позиция1," +
//                    "Позиция2,Позиция3,Позиция4,Позиция5,Позиция6]]";

            if (!Files.exists(Path.of(pathValue))) {
                new File(pathValue).mkdirs();
            }
            // Create a process builder for Dart AOT runtime with multiple arguments
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "dartaotruntime",
                    dartScriptPath,
                    subCommand,
                    pathArg,
                    pathValue,
                    invoiceNumberArg,
                    invoiceName,
                    customerDataArg,
                    companyData,
                    deliveryAddressArg,
                    deliveryAddressValue,
                    deliveryPriceArg,
                    deliveryPriceValue,
                    itogoArg,
                    itogoValue,
                    ndsArg,
                    ndsValue,
                    couponArg,
                    couponValue,
                    tovaryListArg,
                    tovaryListValue
            );

            // Redirect standard output and standard error to DISCARD
            processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            processBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);

            // Start the Dart AOT process
            Process process = processBuilder.start();

            // Wait for the Dart AOT process to exit
            int exitCode = process.waitFor();
//            System.out.println("Dart AOT process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return pathValue + invoiceName + PDF;
    }

    public String createPriceList(int customerId, String fileNameValue, String itogoValue, String ndsValue, String tovaryListValue) {
        String pathValue = ".\\"  + customerId + "\\";
        try {

            String pathArg = "--savePath";

            // Additional command-line arguments
            String subCommand = "priceList_gen";

            String fileNameArg = "--fileName";
//            fileNameValue = "priceList";

            String itogoArg = "--itogo";
//            itogoValue = "1 961,61";

            String ndsArg = "--nds";
//            ndsValue = "365,00";

            String tovaryListArg = "--tovaryList";
//          [Num of a product in the list,fullTitle of modification,amount of a product,price,sum,delivery time]
//            tovaryListValue = "[[Позиция1,Позиция2,Позиция3,Позиция4,Позиция5,Позиция6]," +
//                    "[Позиция1,Позиция2,Позиция3,Позиция4,Позиция5,Позиция6]," +
//                    "[Позиция1,Позиция2,Позиция3,Позиция4,Позиция5,Позиция6]]";

            if (!Files.exists(Path.of(pathValue))) {
                new File(pathValue).mkdirs();
            }

            // Create a process builder for Dart with multiple arguments
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "dartaotruntime",
                    dartScriptPath,
                    subCommand,
                    pathArg,
                    pathValue,
                    fileNameArg,
                    fileNameValue,
                    itogoArg,
                    itogoValue,
                    ndsArg,
                    ndsValue,
                    tovaryListArg,
                    tovaryListValue
            );

            // Redirect standard output and standard error to DISCARD
            processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            processBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);

            // Start the Dart process
            Process process = processBuilder.start();

            // Wait for the Dart process to exit
            int exitCode = process.waitFor();
            System.out.println("Dart process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return pathValue + fileNameValue + PDF;
    }
}
