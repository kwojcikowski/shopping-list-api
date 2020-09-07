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

    @Override
    public Product deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String name = String.valueOf(node.get("product").get("name").asText());
        Unit defaultUnit = deserializationContext.readValue(node.get("defaultUnit").traverse(oc), Unit.class);
        Section section = deserializationContext.readValue(node.get("section").traverse(oc), Section.class);
        //In case of resolving existing product (passed with _links property)
        if (node.has("_links")) {
            String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
            String[] parts = url.split("/");
            Long id = Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
            return new Product(id, name, defaultUnit, section);
        }
        return new Product(name, defaultUnit, section);
    }
}
