package com.example.antonapi.service;

import com.example.antonapi.model.Product;
import com.example.antonapi.service.dto.ImageDTO;
import com.example.antonapi.service.dto.ProductDTO;
import com.example.antonapi.service.exception.ProductException;

import java.io.IOException;

public interface ProductService {
    Iterable<Product> getAllProducts();
    Product findProduct(Long id);
    Product addProduct(Product product) throws ProductException;
    Product updateProduct(Product product) throws ProductException;
    void deleteProductById(Long id) throws ProductException;
    Product registerNewProduct(ProductDTO productDTO) throws ProductException, IOException;
}
