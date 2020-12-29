package com.example.antonapi.service.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonStringFormatter {

    public static String prettify(String unformattedJsonString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(unformattedJsonString);
        return gson.toJson(je);
    }
}
