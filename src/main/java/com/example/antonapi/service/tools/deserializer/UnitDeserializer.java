package com.example.antonapi.service.tools.deserializer;

import com.example.antonapi.model.BaseUnit;
import com.example.antonapi.model.Prefix;
import com.example.antonapi.model.Unit;
import com.google.gson.*;

import java.lang.reflect.Type;

public class UnitDeserializer implements JsonDeserializer<Unit> {

    @Override
    public Unit deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();
        Long id = root.get("id").getAsLong();
        BaseUnit baseUnit = jsonDeserializationContext.deserialize(root.get("baseUnit"), BaseUnit.class);
        Prefix prefix = jsonDeserializationContext.deserialize(root.get("prefix"), Prefix.class);
        JsonElement masterUnitNode = root.get("masterUnit");
        if(masterUnitNode == null)
            return new Unit(id, baseUnit, prefix, null);
        return new Unit(id, baseUnit, prefix, jsonDeserializationContext.deserialize(masterUnitNode, Unit.class));
    }
}
