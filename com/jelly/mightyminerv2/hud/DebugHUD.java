/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.config.core.OneColor
 *  cc.polyfrost.oneconfig.hud.TextHud
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.opengl.Display
 */
package com.jelly.mightyminerv2.hud;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.TextHud;
import com.jelly.mightyminerv2.handler.GameStateHandler;
import com.jelly.mightyminerv2.util.ScoreboardUtil;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class DebugHUD
extends TextHud {
    private static transient DebugHUD instance = new DebugHUD();
    private transient Minecraft mc = Minecraft.func_71410_x();

    public static DebugHUD getInstance() {
        return instance;
    }

    public DebugHUD() {
        super(true, 1.0f, 10.0f, 0.8f, true, true, 1.0f, 5.0f, 5.0f, new OneColor(0, 0, 0, 150), false, 2.0f, new OneColor(0, 0, 0, 127));
    }

    protected void getLines(List<String> lines, boolean example) {
        lines.add("\u00a76\u00a7lScoreboard");
        lines.add("\u00a77Title: \u00a7f" + ScoreboardUtil.getScoreboardTitle());
        lines.addAll(ScoreboardUtil.getScoreboard());
        lines.add("");
        lines.add("\u00a76\u00a7lPlayer Location");
        lines.add("\u00a77Current Location: \u00a7f" + GameStateHandler.getInstance().getCurrentLocation().getName());
        lines.add("\u00a77Sub Location: \u00a7f" + GameStateHandler.getInstance().getCurrentSubLocation().getName());
        lines.add("");
        lines.add("\u00a76\u00a7lDisplay & Game State");
        lines.add("\u00a77In-Game Focus: \u00a7f" + this.mc.field_71415_G);
        lines.add("\u00a77Display Active: \u00a7f" + Display.isActive());
    }
}

