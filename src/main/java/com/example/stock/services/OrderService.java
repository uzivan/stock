package com.example.stock.services;

import com.example.stock.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public List<Order> findAll();
    public Optional<Order> findById(int id);
    public Order save(Order order);

    public void delete(Order order);
}
