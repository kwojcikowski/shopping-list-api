package com.example.antonapi.service.impl;

import com.example.antonapi.model.Product;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.service.ProductService;
import com.example.antonapi.service.exception.ProductException;
import com.example.antonapi.service.tools.ImagesTools;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final @NonNull ProductRepository productRepository;

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProduct(Long id) throws ProductException {
        if(productRepository.existsById(id))
            return productRepository.findProductById(id);
        throw new ProductException("Unable to fetch product: Product with id " + id + " does not exist.");
    }

    @Override
    public void deleteProductById(Long id) throws ProductException {
        if(productRepository.existsById(id))
            productRepository.deleteById(id);
        else
            throw new ProductException("Unable to delete product: Product with ID " + id + " does not exist.");
    }

    @Override
    public Product registerNewProduct(Product product, String imageUrl) throws ProductException, IOException {
        if(product.getId() != null)
            throw new ProductException("Unable to register product: Product must not have an ID.");
        if(productRepository.findProductByName(product.getName()) != null)
            throw new ProductException("Unable to register product: Product with name "
                    + product.getName()+ " already exists.");

        //Image valid to save, so generate image from given URL
        //Extract image data
        String fileType = imageUrl.substring(imageUrl.lastIndexOf('.'));

        //Find src/main/resources/img folder
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "img");
        String systemFriendlyProductName = product.getName().replaceAll(" ", "_").replaceAll("[^a-zA-Z_]", "");
        String imagePath = path.toAbsolutePath().toString() + File.separator +
                systemFriendlyProductName + fileType;

        File image = ImagesTools.saveImageFromURL(imageUrl, imagePath);
        File thumbImage = ImagesTools.generateImageThumbnail(image);

        product.setImage(image);
        product.setThumbImage(thumbImage);
        return productRepository.saveAndFlush(product);
    }
}
