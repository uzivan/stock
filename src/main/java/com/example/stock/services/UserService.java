package com.example.stock.services;

import com.example.stock.entities.User;

public interface UserService {
    public User findByUsername(String username);
}
