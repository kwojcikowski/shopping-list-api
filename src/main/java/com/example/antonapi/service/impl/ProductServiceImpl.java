package com.example.antonapi.service.impl;

import com.example.antonapi.model.Product;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.service.ProductService;
import com.example.antonapi.service.dto.ImageDTO;
import com.example.antonapi.service.dto.ProductDTO;
import com.example.antonapi.service.exception.ProductException;
import com.example.antonapi.service.tools.ImagesTools;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
    public Product findProduct(Long id) {
        return productRepository.findProductById(id);
    }

    @Override
    public Product addProduct(Product product) throws ProductException {
        Product existingProduct = productRepository.findProductByName(product.getName());
        if(existingProduct == null)
            return productRepository.saveAndFlush(product);
        throw new ProductException("Unable to add product: Product with name " + product.getName() + " already exists.");
    }

    @Override
    public void deleteProductById(Long id) throws ProductException {
        if(productRepository.existsById(id))
            productRepository.deleteById(id);
        else
            throw new ProductException("Unable to delete product: Product with ID " + id + " does not exist.");
    }

    @Override
    public Product registerNewProduct(ProductDTO productDTO) throws ProductException, IOException {
        if(productDTO.getId() != null)
            throw new ProductException("Unable to register product: Product must not have an ID.");
        if(productRepository.findProductByName(productDTO.getName()) != null)
            throw new ProductException("Unable to register product: Product with name "
                    + productDTO.getName()+ " already exists.");

        //Image valid to save, so generate image from given URL
        //Extract image data
        String urlString = productDTO.getImage();
        String fileType = urlString.substring(urlString.lastIndexOf('.'));

        //Find src/main/resources/img folder
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "img");
        String systemFriendlyProductName = productDTO.getName().replaceAll(" ", "_").replaceAll("[^a-zA-Z_]", "");
        String imagePath = path.toAbsolutePath().toString() + File.separator +
                systemFriendlyProductName + fileType;

        File image = ImagesTools.saveImageFromURL(urlString, imagePath);
        File thumbImage = ImagesTools.generateImageThumbnail(image);

        ModelMapper modelMapper = new ModelMapper();
        Product productFromDTO = modelMapper.map(productDTO, Product.class);
        productFromDTO.setImage(image);
        productFromDTO.setThumbImage(thumbImage);

        return productFromDTO;
    }
}
