package com.jack.codingTask.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jack.codingTask.csv_helper.CSVProductHelper;
import com.jack.codingTask.model.Product;
import com.jack.codingTask.repository.ProductRepository;

@Service
public class CSVProductService {
  @Autowired
  ProductRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Product> products = CSVProductHelper.csvToProducts(file.getInputStream());
      repository.saveAll(products);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<Product> products = repository.findAll();

    ByteArrayInputStream in = CSVProductHelper.productsToCSV(products);
    return in;
  }

}