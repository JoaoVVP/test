/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.util.helper.location;

import java.util.HashMap;
import java.util.Map;

public enum Location {
    PRIVATE_ISLAND("Private Island"),
    HUB("Hub"),
    THE_PARK("The Park"),
    THE_FARMING_ISLANDS("The Farming Islands"),
    SPIDER_DEN("Spider's Den"),
    THE_END("The End"),
    CRIMSON_ISLE("Crimson Isle"),
    GOLD_MINE("Gold Mine"),
    DEEP_CAVERNS("Deep Caverns"),
    DWARVEN_MINES("Dwarven Mines"),
    CRYSTAL_HOLLOWS("Crystal Hollows"),
    JERRY_WORKSHOP("Jerry's Workshop"),
    DUNGEON_HUB("Dungeon Hub"),
    GARDEN("Garden"),
    DUNGEON("Dungeon"),
    LIMBO("UNKNOWN"),
    LOBBY("PROTOTYPE"),
    KNOWHERE("Knowhere");

    private final String name;
    private static final Map<String, Location> nameToLocationMap;

    private Location(String name) {
        this.name = name;
    }

    public static Location fromName(String name) {
        Location loc = nameToLocationMap.get(name);
        if (loc == null) {
            return KNOWHERE;
        }
        return loc;
    }

    public String getName() {
        return this.name;
    }

    static {
        nameToLocationMap = new HashMap<String, Location>();
        for (Location location : Location.values()) {
            nameToLocationMap.put(location.getName(), location);
        }
    }
}

