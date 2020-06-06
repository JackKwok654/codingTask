package com.jack.codingTask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jack.codingTask.model.Stock;
import com.jack.codingTask.model.StockKey;

public interface StockRepository extends JpaRepository<Stock, StockKey> {
  List<Stock> findByLocationContaining(String location);
  List<Stock> findByCodeContaining(String code);
}