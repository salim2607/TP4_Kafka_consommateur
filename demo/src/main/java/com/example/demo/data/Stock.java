package com.example.demo.data;

import java.util.HashMap;
import java.util.Map;

public class Stock {
    private Map<String, Integer> stock = new HashMap<>();

    public Stock() {
    }   

    public Map<String, Integer> getStock() {
        return stock;
    }

    public void setStock(Map<String, Integer> stock) {
        this.stock = stock;
    }

    public void addStock(String stockName, Integer quantity) {
        stock.put(stockName, stock.getOrDefault(stockName, 0) + quantity);
    }
}
