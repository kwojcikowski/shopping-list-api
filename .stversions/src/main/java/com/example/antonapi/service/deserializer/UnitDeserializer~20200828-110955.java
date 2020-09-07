package com.example.antonapi.service.deserializer;

import com.example.antonapi.model.BaseUnit;
import com.example.antonapi.model.Prefix;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.BaseUnitRepository;
import com.example.antonapi.repository.PrefixRepository;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.RepositoryService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class UnitDeserializer extends JsonDeserializer<Unit> {
    private final UnitRepository unitRepository = (UnitRepository) RepositoryService.getRepos().get(Unit.class);

    @Override
    public Unit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        try {
            String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
            String[] parts = url.split("/");
            Long id = Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
            return unitRepository.getOne(id);
        } catch (NullPointerException exception){
            //Currently there is only support for using existing units.
            throw new IOException("Non-existing unit object passed.");
        }
    }
}
