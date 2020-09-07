package com.example.antonapi.service.deserializer;

import com.fasterxml.jackson.databind.JsonNode;

public class UrlParser {

    static Long extractIdFromObject(JsonNode node){
        String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
        String[] parts = url.split("/");
        return Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
    }
}
