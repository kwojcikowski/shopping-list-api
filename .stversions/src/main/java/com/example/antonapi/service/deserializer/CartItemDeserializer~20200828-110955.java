package com.example.antonapi.service.deserializer;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Product;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.CartItemRepository;
import com.example.antonapi.service.RepositoryService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

public class CartItemDeserializer extends JsonDeserializer<CartItem> {

    @Override
    public CartItem deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // In case of no-ID cartItem resolve manually
        BigDecimal quantity = new BigDecimal(node.get("quantity").asText());
        Product product = deserializationContext.readValue(node.get("product").traverse(oc), Product.class);
        Unit unit = deserializationContext.readValue(node.get("unit").traverse(oc), Unit.class);
        //In case of resolving existing cartItem (passed with _links property)
        if (node.has("_links")) {
            String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
            String[] urlParts = url.split("/");
            Long id = Long.valueOf(urlParts[urlParts.length-1].replace("{?projection}", ""));
            return new CartItem(id, product, unit, quantity);
        }
        return new CartItem(product, unit, quantity);
    }
}
