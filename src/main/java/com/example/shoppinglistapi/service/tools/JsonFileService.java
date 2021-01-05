package com.example.shoppinglistapi.service.tools;

import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.service.tools.deserializer.UnitDeserializer;
import com.google.gson.*;
import org.atteo.evo.inflector.English;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonFileService {

    private static Gson gson = new GsonBuilder().registerTypeAdapter(Unit.class, new UnitDeserializer()).create();

    public static <T> List<T> loadCollectionFromFile(Path filePath, Class<T> clazz) throws IOException {
        List<T> parsedObjects = new ArrayList<>();

        //The collection key is thought to be `camelCase` name of class
        String className = clazz.getSimpleName();
        String pluralClassName = English.plural(className, 2);
        String collectionKey = Character.toLowerCase(pluralClassName.charAt(0)) + pluralClassName.substring(1);

        JsonElement rootElement = parseFileIntoJsonObject(filePath);
        JsonArray collection = rootElement.getAsJsonObject().getAsJsonArray(collectionKey);
        if(collection == null)
            throw new JsonIOException("Could not find collection of key " + collectionKey + ".");
        for(int i = 0; i < collection.size(); i++){
            parsedObjects.add(gson.fromJson(collection.get(i), clazz));
        }
        return parsedObjects;
    }

    private static JsonElement parseFileIntoJsonObject(Path filePath) throws IOException {
        JsonElement element;
        try {
            element = JsonParser.parseReader(new FileReader(filePath.toAbsolutePath().toFile()));
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("Unable to read " + filePath.toAbsolutePath() + ".");
        } catch (JsonIOException | JsonSyntaxException e){
            throw new JsonIOException("Unable to parse " + filePath.toAbsolutePath() + " as json file.");
        }
        return element;
    }
}
