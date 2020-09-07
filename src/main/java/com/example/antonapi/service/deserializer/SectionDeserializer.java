package com.example.antonapi.service.deserializer;

import com.example.antonapi.model.Section;
import com.example.antonapi.repository.SectionRepository;
import com.example.antonapi.service.RepositoryService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SectionDeserializer extends JsonDeserializer<Section> {

    private SectionRepository sectionRepository = (SectionRepository) RepositoryService.getRepos().get(Section.class);

    @Override
    public Section deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        try {
            Long id = UrlParser.extractIdFromObject(node);
            return sectionRepository.getOne(id);
        } catch (NullPointerException exception){
            //Currently there is only support for using existing sections.
            throw new IOException("Non-existing section object passed.");
        }
    }
}
