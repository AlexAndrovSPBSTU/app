package ru.owen.app.util;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Dart {

    public static void main(String[] args) {
//        createPriceList("fileName", "20", "100", "[[1,2,3,4,5,6]]");
//        createInvoice("", "", "", "", "", "", "");
    }

    public void createInvoice(String invoiceName, String companyData, String deliveryAddressValue, String deliveryPriceValue,
                              String itogoValue, String ndsValue, String couponValue, String tovaryListValue) {
        try {
            // Specify the Dart script file path
            String dartScriptPath = "D:\\OWEN\\app\\src\\main\\resources\\main.aot";

            // Additional command-line arguments
            String subCommand = "invoice_gen";

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

            // Create a process builder for Dart AOT runtime with multiple arguments
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "dartaotruntime",
                    dartScriptPath,
                    subCommand,
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
            System.out.println("Dart AOT process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createPriceList(String fileNameValue, String itogoValue, String ndsValue, String tovaryListValue) {
        try {
            // Specify the Dart script file path
            String dartScriptPath = "D:\\OWEN\\app\\src\\main\\resources\\main.aot";


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

            // Create a process builder for Dart with multiple arguments
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "dartaotruntime",
                    dartScriptPath,
                    subCommand,
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
    }
}
