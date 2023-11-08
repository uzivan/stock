package com.example.stock.services;

import com.example.stock.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public List<Product> findAll();
    public Optional<Product> findById(int id);

    public Product save(Product product);

    public void delete(Product product);
}
