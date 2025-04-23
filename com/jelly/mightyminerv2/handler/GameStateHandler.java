/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.jelly.mightyminerv2.handler;

import com.jelly.mightyminerv2.event.UpdateScoreboardEvent;
import com.jelly.mightyminerv2.event.UpdateTablistEvent;
import com.jelly.mightyminerv2.event.UpdateTablistFooterEvent;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.ScoreboardUtil;
import com.jelly.mightyminerv2.util.helper.location.Location;
import com.jelly.mightyminerv2.util.helper.location.SubLocation;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameStateHandler {
    private static GameStateHandler instance = new GameStateHandler();
    private final Minecraft mc = Minecraft.func_71410_x();
    private final Pattern areaPattern = Pattern.compile("Area:\\s(.+)");
    private String serverIp = "";
    private Location currentLocation = Location.KNOWHERE;
    private SubLocation currentSubLocation = SubLocation.KNOWHERE;
    private boolean godpotActive = false;
    private boolean cookieActive = false;

    public static GameStateHandler getInstance() {
        return instance;
    }

    public boolean isPlayerInSkyBlock() {
        return this.currentLocation.ordinal() < Location.values().length - 3;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.currentLocation = Location.KNOWHERE;
        this.currentSubLocation = SubLocation.KNOWHERE;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (this.mc.func_147104_D() != null && this.mc.func_147104_D().field_78845_b != null) {
            this.serverIp = this.mc.func_147104_D().field_78845_b;
        }
    }

    @SubscribeEvent
    public void onTablistUpdate(UpdateTablistEvent event) {
        if (event.tablist.isEmpty()) {
            return;
        }
        List<String> tabList = event.tablist;
        List<String> scoreboard = ScoreboardUtil.getScoreboard();
        if (tabList.size() == 1 && InventoryUtil.isInventoryEmpty()) {
            this.currentLocation = Location.LIMBO;
            this.currentSubLocation = SubLocation.KNOWHERE;
            return;
        }
        for (String tabline : tabList) {
            if (!tabline.startsWith("Area: ")) continue;
            Matcher matcher = this.areaPattern.matcher(tabline);
            if (!matcher.find()) {
                return;
            }
            String area = matcher.group(1);
            this.currentLocation = Location.fromName(area);
            return;
        }
        if (!ScoreboardUtil.getScoreboardTitle().contains("SKYBLOCK") && !scoreboard.isEmpty() && scoreboard.get(scoreboard.size() - 1).equalsIgnoreCase("www.hypixel.net")) {
            this.currentLocation = Location.LOBBY;
            return;
        }
        this.currentLocation = Location.KNOWHERE;
    }

    @SubscribeEvent
    public void onTablistFooterUpdate(UpdateTablistFooterEvent event) {
        List<String> footer = event.footer;
        for (int i = 0; i < footer.size(); ++i) {
            if (footer.get(i).contains("Active Effects")) {
                this.godpotActive = footer.get(++i).contains("You have a God Potion active!");
            }
            if (!footer.get(i).contains("Cookie Buff")) continue;
            this.cookieActive = !footer.get(++i).contains("Not active!");
            break;
        }
    }

    @SubscribeEvent
    public void onScoreboardListUpdate(UpdateScoreboardEvent event) {
        for (int i = 0; i < event.scoreboard.size(); ++i) {
            String line = event.scoreboard.get(i);
            if (!line.contains("\u23e3") && !line.contains("\u0444")) continue;
            this.currentSubLocation = SubLocation.fromName(ScoreboardUtil.sanitizeString(line).trim());
            break;
        }
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public SubLocation getCurrentSubLocation() {
        return this.currentSubLocation;
    }

    public boolean isGodpotActive() {
        return this.godpotActive;
    }

    public boolean isCookieActive() {
        return this.cookieActive;
    }
}

