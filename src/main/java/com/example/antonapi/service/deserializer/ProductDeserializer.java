package com.example.antonapi.service.deserializer;

import com.example.antonapi.model.Product;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ProductDeserializer extends JsonDeserializer<Product> {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        //In case of resolving existing product (passed with _links property)
        if (node.has("_links")) {
            Long id = UrlParser.extractIdFromObject(node);
            return productRepository.findProductById(id);
        }

        //In case of product with no id
        String name = String.valueOf(node.get("product").get("name").asText());
        Unit defaultUnit = deserializationContext.readValue(node.get("defaultUnit").traverse(oc), Unit.class);
        Section section = deserializationContext.readValue(node.get("section").traverse(oc), Section.class);

        //Download selected image
        String urlString = node.get("image").asText();
        String fileType = urlString.substring(urlString.lastIndexOf('.'));
        URL imageUrl = new URL(urlString);

        //Find src/main/resources/img folder
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "img");
        String systemFriendlyProductName = name.replaceAll(" ", "_").replaceAll("[^a-zA-Z_]", "");
        String imagePath = path.toAbsolutePath().toString() + File.separator +
                 systemFriendlyProductName + fileType;

        ReadableByteChannel readableByteChannel = Channels.newChannel(imageUrl.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        readableByteChannel.close();
        fileOutputStream.close();
        fileChannel.close();

        //Now when image is saved create its thumbnail
        int thumbnailHeight = 50;
        int thumbnailWidth = 50;
        String thumbImagePath = path.toAbsolutePath().toString() + File.separator +
                systemFriendlyProductName + "_thumbnail" + fileType;
        try {
            BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
            img.createGraphics().drawImage(ImageIO.read(new File(imagePath)).getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH),0,0,null);
            File outputFile=new File(thumbImagePath);
            ImageIO.write(img, "jpg", outputFile);
        } catch (IOException e) {
            System.out.println("Exception while generating thumbnail "+e.getMessage());
        }

        return new Product(name, defaultUnit, section, new File(imagePath), new File(thumbImagePath));
    }
}
