package com.jack.codingTask.csv_helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.jack.codingTask.model.Product;

public class CSVProductHelper {
  
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "code", "name", "weight" };

  public static boolean hasCSVFormat(MultipartFile file) {

    /*if (!TYPE.equals(file.getContentType())) {
      return false;
    }*/

    return true;
  }

  public static List<Product> csvToProducts(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Product> products = new ArrayList<Product>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        Product product = new Product(
        	  csvRecord.get("code"),
        	  csvRecord.get("name"),
              Long.parseLong(csvRecord.get("weight"))
            );

        products.add(product);
      }

      return products;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream productsToCSV(List<Product> products) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

      csvPrinter.printRecord(HEADERs);

      for (Product product : products) {
        List<String> data = Arrays.asList(
              product.getCode(),
              product.getName(),
              String.valueOf(product.getWeight())
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }

}