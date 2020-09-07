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
    private UnitRepository unitRepository = (UnitRepository) RepositoryService.getRepos().get(Unit.class);
    private BaseUnitRepository baseUnitRepository = (BaseUnitRepository) RepositoryService.getRepos().get(BaseUnit.class);
    private PrefixRepository prefixRepository = (PrefixRepository) RepositoryService.getRepos().get(Prefix.class);

    @Override
    public Unit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        //In case of resolving existing unit (passed with _links property) - prevents infinite recursion
        if (node.has("_links")) {
            String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
            String[] parts = url.split("/");
            Long id = Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
            return unitRepository.getOne(id);
        }
        //In case of resolving non-ID object
        BaseUnit baseUnit = baseUnitRepository.getOne(node.get("baseUnit").get("id").asLong());
        Prefix prefix = prefixRepository.getOne(node.get("prefix").get("id").asLong());
        String masterUnitUrl = node.get("masterUnit").get("self").get("href").toString().replace("\"", "");
        String[] parts = url.split("/");
        Long id = Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
        Unit masterUnit = unitRepository.getOne(node.get("masterUnit").get("id").asLong());

        return new Unit(baseUnit, prefix, masterUnit);
    }
}
