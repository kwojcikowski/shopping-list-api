package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.dto.product.ProductCreateDto;
import com.example.shoppinglistapi.model.Product;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

public interface ProductService {
    Iterable<Product> getAllProducts();

    Product findProduct(Long id);

    void deleteProductById(Long id);

    Product registerNewProduct(ProductCreateDto createDto) throws DataIntegrityViolationException,
            EntityNotFoundException, IOException;
}
