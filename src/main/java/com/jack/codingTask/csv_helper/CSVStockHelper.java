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

import com.jack.codingTask.model.Stock;
//import com.jack.codingTask.model.Product;

public class CSVStockHelper {
  
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "location", "code", "quantity" };

  public static boolean hasCSVFormat(MultipartFile file) {

    /*if (!TYPE.equals(file.getContentType())) {
      return false;
    }*/

    return true;
  }

  public static List<Stock> csvToStocks(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Stock> stocks = new ArrayList<Stock>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {     	
        Stock stock = new Stock(
        	  csvRecord.get("location"),
        	  csvRecord.get("code"),
              Long.parseLong(csvRecord.get("quantity"))
            );

        stocks.add(stock);
      }

      return stocks;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream stocksToCSV(List<Stock> stocks) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      
      csvPrinter.printRecord(HEADERs);

      for (Stock stock : stocks) {
        List<String> data = Arrays.asList(
              stock.getLocation(),
              stock.getCode(),
              String.valueOf(stock.getQuantity())
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