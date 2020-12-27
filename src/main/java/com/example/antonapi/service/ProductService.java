package com.example.antonapi.service;

import com.example.antonapi.model.Product;
import com.example.antonapi.service.dto.ImageDTO;
import com.example.antonapi.service.dto.ProductDTO;
import com.example.antonapi.service.exception.ProductException;

import java.io.IOException;
import java.util.Optional;

public interface ProductService {
    Iterable<Product> getAllProducts();
    Product findProduct(Long id) throws ProductException;
    void deleteProductById(Long id) throws ProductException;
    Product registerNewProduct(Product product, String imageUrl) throws ProductException, IOException;
}
