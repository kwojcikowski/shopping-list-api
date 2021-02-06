package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.product.ProductCreateDto;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.repository.ProductRepository;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.tools.ImagesTools;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final @NonNull ProductRepository productRepository;
    private final @NonNull UnitRepository unitRepository;
    private final @NonNull SectionRepository sectionRepository;

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unable to fetch product: " +
                        "Product with given id does not exist."));
    }

    @Override
    public void deleteProductById(Long id) {
        if (productRepository.existsById(id))
            productRepository.deleteById(id);
        else
            throw new EntityNotFoundException("Unable to delete product: " +
                    "Product with given id does not exist.");
    }

    @Override
    public Product registerNewProduct(ProductCreateDto createDto) throws DataIntegrityViolationException,
            EntityNotFoundException, IOException {

        if (productRepository.existsByName(createDto.name))
            throw new DataIntegrityViolationException("Unable to register product: Product with name "
                    + createDto.name + " already exists.");
        Unit defaultUnit = unitRepository.findById(createDto.defaultUnitId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to register product: Provided unit does not exist."));
        Section section = sectionRepository.findById(createDto.sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to register product: Provided section does not exist."));

        String fileType = createDto.imageUrl.substring(createDto.imageUrl.lastIndexOf('.'));

        String systemFriendlyProductName =
                createDto.name.replaceAll(" ", "_").replaceAll("[^a-zA-Z_]", "");
        String fileSuffix = new SimpleDateFormat("-yyyyMMddHHmmss").format(new Date());
        String imagePath = systemFriendlyProductName + fileSuffix + fileType;

        File image;
        File thumbImage;
        try {
            image = ImagesTools.saveImageFromURL(createDto.imageUrl, imagePath);
            thumbImage = ImagesTools.generateImageThumbnail(image);
        } catch (IOException e) {
            throw new IOException("Unable to register product: Error saving the image.");
        }

        Product product = Product.builder()
                .name(createDto.name)
                .defaultUnit(defaultUnit)
                .section(section)
                .image(image)
                .thumbImage(thumbImage)
                .build();
        return productRepository.saveAndFlush(product);
    }
}
