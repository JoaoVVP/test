/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.google.common.collect.ImmutableSet;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.handler.GameStateHandler;
import com.jelly.mightyminerv2.util.helper.location.Location;
import com.jelly.mightyminerv2.util.helper.location.SubLocation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoWarp
extends AbstractFeature {
    private static AutoWarp instance;
    private Error failReason = Error.NONE;
    private int attempts = 0;
    private boolean nosbError = false;
    private Location targetLocation = null;
    private SubLocation targetSubLocation = null;
    private final Set<String> waitMessages = ImmutableSet.of((Object)"Couldn't warp you! Try again later.", (Object)"Cannot join SkyBlock for a moment!", (Object)"You are sending commands too fast! Please slow down.", (Object)"You were kicked while joining that server!", (Object)"You tried to rejoin too fast, please try again in a moment.");
    private final String playerNotOnSkyBlock = "Oops! You are not on SkyBlock so we couldn't warp you!";
    private final String noWarpScroll = "You haven't unlocked this fast travel destination!";
    private final Map<Location, String> LOCATION_WARP_COMMANDS = new HashMap<Location, String>(){
        {
            this.put(Location.PRIVATE_ISLAND, "island");
            this.put(Location.HUB, "hub");
            this.put(Location.THE_PARK, "park");
            this.put(Location.THE_FARMING_ISLANDS, "barn");
            this.put(Location.SPIDER_DEN, "spider");
            this.put(Location.THE_END, "end");
            this.put(Location.CRIMSON_ISLE, "isle");
            this.put(Location.GOLD_MINE, "gold");
            this.put(Location.DEEP_CAVERNS, "deep");
            this.put(Location.DWARVEN_MINES, "mines");
            this.put(Location.CRYSTAL_HOLLOWS, "ch");
            this.put(Location.JERRY_WORKSHOP, "/savethejerrys");
            this.put(Location.DUNGEON_HUB, "dhub");
            this.put(Location.LOBBY, "/l");
            this.put(Location.GARDEN, "garden");
        }
    };
    private final Map<SubLocation, String> SUBLOCATION_WARP_COMMANDS = new HashMap<SubLocation, String>(){
        {
            this.put(SubLocation.MUSEUM, "museum");
            this.put(SubLocation.RUINS, "castle");
            this.put(SubLocation.MUSHROOM_DESERT, "desert");
            this.put(SubLocation.TRAPPERS_DEN, "trapper");
            this.put(SubLocation.HOWLING_CAVE, "howl");
            this.put(SubLocation.JUNGLE_ISLAND, "jungle");
            this.put(SubLocation.THE_FORGE, "forge");
            this.put(SubLocation.CRYSTAL_NUCLEUS, "nucleus");
            this.put(SubLocation.SPIDER_MOUND, "top");
            this.put(SubLocation.ARACHNES_SANCTUARY, "arachne");
            this.put(SubLocation.DRAGONS_NEST, "drag");
            this.put(SubLocation.VOID_SEPULTURE, "void");
            this.put(SubLocation.FORGOTTEN_SKULL, "skull");
            this.put(SubLocation.SMOLDERING_TOMB, "tomb");
            this.put(SubLocation.THE_WASTELAND, "wasteland");
            this.put(SubLocation.DRAGONTAIL, "dragontail");
            this.put(SubLocation.SCARLETON, "scarleton");
        }
    };

    public static AutoWarp getInstance() {
        if (instance == null) {
            instance = new AutoWarp();
        }
        return instance;
    }

    public AutoWarp() {
        this.failsafesToIgnore = Arrays.asList(AbstractFailsafe.Failsafe.values());
    }

    @Override
    public String getName() {
        return "AutoWarp";
    }

    public void start(Location targetLocation, SubLocation targetSubLocation) {
        if (targetLocation != null && !this.LOCATION_WARP_COMMANDS.containsKey((Object)targetLocation) || targetSubLocation != null && !this.SUBLOCATION_WARP_COMMANDS.containsKey((Object)targetSubLocation)) {
            this.error("Warp Scroll for " + (Object)((Object)targetLocation) + " or " + (Object)((Object)targetSubLocation) + " does not exist.");
            return;
        }
        this.targetLocation = targetLocation;
        this.targetSubLocation = targetSubLocation;
        this.failReason = Error.NONE;
        this.enabled = true;
        this.start();
        this.log("Enabled");
    }

    @Override
    public void stop() {
        this.enabled = false;
        this.attempts = 0;
        this.targetLocation = null;
        this.targetSubLocation = null;
        this.nosbError = false;
        this.log("Disabled");
    }

    @Override
    public boolean shouldNotCheckForFailsafe() {
        return true;
    }

    public void stop(Error failReason) {
        this.failReason = failReason;
        this.stop();
    }

    public boolean hasSucceeded() {
        return !this.enabled && this.failReason == Error.NONE;
    }

    public Error getFailReason() {
        return this.failReason;
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        if (this.attempts > 10) {
            this.stop(Error.FAILED_TO_WARP);
            this.log("Failed to Auto Warp.");
            return;
        }
        if (this.isDoneWarping()) {
            this.stop();
            this.log("Done Warping");
            return;
        }
        if (this.timer.isScheduled() && !this.timer.passed()) {
            return;
        }
        ++this.attempts;
        this.timer.schedule(5000L);
        Location currentLocation = GameStateHandler.getInstance().getCurrentLocation();
        SubLocation currentSubLocation = GameStateHandler.getInstance().getCurrentSubLocation();
        if (this.nosbError) {
            this.nosbError = false;
            this.sendCommand(this.getWarpCommand(Location.LOBBY));
            this.log("Not On SkyBlock error");
            return;
        }
        if (!GameStateHandler.getInstance().isPlayerInSkyBlock()) {
            if (currentLocation == Location.LIMBO) {
                this.sendCommand(this.getWarpCommand(Location.LOBBY));
            } else {
                this.sendCommand("/play sb");
            }
            this.log("Player is not on skyblock.");
            return;
        }
        if (this.targetLocation != null && this.targetSubLocation == null && this.targetLocation != currentLocation) {
            String warpCommand = this.getWarpCommand(this.targetLocation);
            this.sendCommand(warpCommand);
            this.log("Player not at island. Sending Command: " + warpCommand);
            return;
        }
        if (this.targetSubLocation != null && this.targetSubLocation != currentSubLocation) {
            String warpCommand = this.getWarpCommand(this.targetSubLocation);
            this.sendCommand(warpCommand);
            this.log("Player not at SubLocation. Sending: " + warpCommand);
            return;
        }
        this.stop(Error.FAILED_TO_WARP);
    }

    @SubscribeEvent
    protected void onChat(ClientChatReceivedEvent event) {
        if (!this.enabled || event.type != 0) {
            return;
        }
        String message = event.message.func_150260_c();
        if (this.waitMessages.contains(message)) {
            this.timer.schedule(10000L);
        }
        if (message.contains(this.playerNotOnSkyBlock)) {
            this.nosbError = true;
        }
        if (message.contains(this.noWarpScroll)) {
            this.stop(Error.NO_SCROLL);
            this.error("Please consume the " + this.targetLocation.getName() + " and " + this.targetSubLocation.getName() + " travel scrolls.");
        }
    }

    public boolean isDoneWarping() {
        Location currentIsland = GameStateHandler.getInstance().getCurrentLocation();
        SubLocation currentSubLocation = GameStateHandler.getInstance().getCurrentSubLocation();
        return !(this.targetLocation != null && currentIsland != this.targetLocation || this.targetSubLocation != null && currentSubLocation != this.targetSubLocation);
    }

    private void sendCommand(String command) {
        if (this.mc.field_71439_g == null) {
            this.note("Player is null. mc : " + (this.mc == null ? "null" : "not null"));
        }
        this.mc.field_71439_g.func_71165_d(command);
    }

    private String getWarpCommand(Location location) {
        String command = this.LOCATION_WARP_COMMANDS.get((Object)location);
        if (command.startsWith("/")) {
            return command;
        }
        return "/warp " + command;
    }

    private String getWarpCommand(SubLocation subLocation) {
        return "/warp " + this.SUBLOCATION_WARP_COMMANDS.get((Object)subLocation);
    }

    public static enum Error {
        NONE,
        FAILED_TO_WARP,
        NO_SCROLL;

    }
}

