package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.service.exception.ProductException;

import java.io.IOException;

public interface ProductService {
    Iterable<Product> getAllProducts();
    Product findProduct(Long id) throws ProductException;
    void deleteProductById(Long id) throws ProductException;
    Product registerNewProduct(Product product, String imageUrl) throws ProductException, IOException;
}
