/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.Scoreboard
 */
package com.jelly.mightyminerv2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class ScoreboardUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static Map<String, SortedMap<Integer, String>> scoreboard = new HashMap<String, SortedMap<Integer, String>>();
    public static String[] scoreObjNames = new String[19];

    public static List<String> getScoreboard() {
        try {
            return new ArrayList<String>(scoreboard.get(scoreObjNames[1]).values());
        }
        catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    public static String getScoreboardTitle() {
        if (ScoreboardUtil.mc.field_71441_e == null) {
            return "";
        }
        Scoreboard scoreboard = ScoreboardUtil.mc.field_71441_e.func_96441_U();
        if (scoreboard == null) {
            return "";
        }
        ScoreObjective objective = scoreboard.func_96539_a(1);
        if (objective == null) {
            return "";
        }
        return ScoreboardUtil.sanitizeString(objective.func_96678_d());
    }

    public static String sanitizeString(String scoreboard) {
        char[] arr = scoreboard.toCharArray();
        StringBuilder cleaned = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            char c = arr[i];
            if (c >= ' ' && c < '\u007f') {
                cleaned.append(c);
            }
            if (c != '\u00a7') continue;
            ++i;
        }
        return cleaned.toString();
    }
}

