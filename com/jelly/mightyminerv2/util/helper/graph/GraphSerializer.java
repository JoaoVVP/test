/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
package com.jelly.mightyminerv2.util.helper.graph;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jelly.mightyminerv2.util.helper.graph.Graph;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GraphSerializer
implements JsonSerializer<Graph<RouteWaypoint>>,
JsonDeserializer<Graph<RouteWaypoint>> {
    public JsonElement serialize(Graph<RouteWaypoint> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject res = new JsonObject();
        JsonObject map = new JsonObject();
        for (Map.Entry entry : src.map.entrySet()) {
            RouteWaypoint waypoint = (RouteWaypoint)entry.getKey();
            String keyString = waypoint.getX() + "," + waypoint.getY() + "," + waypoint.getZ() + "," + waypoint.getTransportMethod().name();
            JsonElement valueElement = context.serialize(entry.getValue());
            map.add(keyString, valueElement);
        }
        res.add("map", (JsonElement)map);
        return res;
    }

    public Graph<RouteWaypoint> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Graph<RouteWaypoint> graph = new Graph<RouteWaypoint>();
        JsonObject map = json.getAsJsonObject().getAsJsonObject("map");
        for (Map.Entry entry : map.entrySet()) {
            try {
                String[] keyParts = ((String)entry.getKey()).split(",");
                if (keyParts.length != 4) {
                    throw new JsonParseException("Invalid RouteWaypoint key format: " + (String)entry.getKey());
                }
                int x = Integer.parseInt(keyParts[0]);
                int y = Integer.parseInt(keyParts[1]);
                int z = Integer.parseInt(keyParts[2]);
                TransportMethod transportMethod = TransportMethod.valueOf(keyParts[3]);
                RouteWaypoint key = new RouteWaypoint(x, y, z, transportMethod);
                List value = (List)context.deserialize((JsonElement)entry.getValue(), new TypeToken<List<RouteWaypoint>>(){}.getType());
                graph.map.put(key, value);
            }
            catch (JsonParseException | NumberFormatException e) {
                System.out.println("Error deserializing entry for key: " + (String)entry.getKey());
                e.printStackTrace();
            }
        }
        return graph;
    }
}

