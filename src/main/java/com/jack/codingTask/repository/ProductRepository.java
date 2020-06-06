package com.jack.codingTask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jack.codingTask.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
  List<Product> findByNameContaining(String name);
  List<Product> findByWeight(long weight);
}