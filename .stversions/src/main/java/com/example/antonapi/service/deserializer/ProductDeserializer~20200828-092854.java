package com.example.antonapi.service.deserializer;

import com.example.antonapi.model.Product;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.repository.SectionRepository;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.RepositoryService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.Arrays;

public class ProductDeserializer extends JsonDeserializer<Product> {

    private ProductRepository productRepository = (ProductRepository) RepositoryService.getRepos().get(Product.class);
    private UnitRepository unitRepository = (UnitRepository) RepositoryService.getRepos().get(Unit.class);
    private SectionRepository sectionRepository = (SectionRepository) RepositoryService.getRepos().get(Section.class);

    @Override
    public Product deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        //In case of resolving existing product (passed with _links property)
        if (node.has("_links")) {
            String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
            String[] parts = url.split("/");
            Long id = Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
            return productRepository.getOne(id);
        }
        // In case of no-ID product resolve manually
        String name = String.valueOf(node.get("name").asText());
        Unit defaultUnit = unitRepository.getOne(node.get("defaultUnit").get("id").asLong());
        Section section = sectionRepository.getOne(node.get("section").get("id").asLong());
        return new Product(name, defaultUnit, section);
    }
}
