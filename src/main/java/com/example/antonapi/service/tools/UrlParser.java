package com.example.antonapi.service.tools;

import com.fasterxml.jackson.databind.JsonNode;

public class UrlParser {

    public static Long extractIdFromObjectBaseNode(JsonNode node){
        String url = node.get("_links").get("self").get("href").toString().replace("\"", "");
        String[] parts = url.split("/");
        return Long.valueOf(parts[parts.length-1].replace("{?projection}", ""));
    }
}
