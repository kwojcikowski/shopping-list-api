package com.example.antonapi.deserializer;

import com.example.antonapi.model.Product;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.service.RepositoryService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ProductDeserializer extends StdDeserializer<Product> {

    private final JsonDeserializer<?> defaultDeserializer;
    ProductRepository productRepository = (ProductRepository) RepositoryService.getRepos().get(Product.class);

    public ProductDeserializer(JsonDeserializer<?> defaultDeserializer, Class<?> clazz){
        super(clazz);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public Product deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        System.out.println(productRepository.findAll());
        //In case of resolving existing product (passed with an id)
        if(node.has("id")){
            Product p = productRepository.getOne(Long.valueOf(String.valueOf(node.get("id"))));
            System.out.println(p);
            return p;
        }
        //In case of resolving product awaiting to add return the default functionality
        // (passed with no id)
        System.out.println("DEFAULT");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        return (Product) defaultDeserializer.deserialize(jsonParser, deserializationContext);
    }
}
