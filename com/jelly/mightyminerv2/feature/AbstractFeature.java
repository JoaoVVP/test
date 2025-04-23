/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature;

import com.jelly.mightyminerv2.event.BlockChangeEvent;
import com.jelly.mightyminerv2.event.BlockDestroyEvent;
import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.event.UpdateTablistEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.Clock;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class AbstractFeature {
    protected final Minecraft mc = Minecraft.func_71410_x();
    protected final Clock timer = new Clock();
    protected List<AbstractFailsafe.Failsafe> failsafesToIgnore = new ArrayList<AbstractFailsafe.Failsafe>();
    protected boolean enabled = false;

    public abstract String getName();

    public boolean isRunning() {
        return this.enabled;
    }

    public List<AbstractFailsafe.Failsafe> getFailsafesToIgnore() {
        return this.failsafesToIgnore;
    }

    public void start() {
    }

    public void stop() {
        this.enabled = false;
        this.resetStatesAfterStop();
    }

    public void pause() {
        this.enabled = false;
    }

    public void resume() {
        this.enabled = true;
    }

    public void resetStatesAfterStop() {
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean shouldStartAtLaunch() {
        return false;
    }

    public boolean shouldNotCheckForFailsafe() {
        return false;
    }

    protected boolean isTimerRunning() {
        return this.timer.isScheduled() && !this.timer.passed();
    }

    protected boolean hasTimerEnded() {
        return this.timer.isScheduled() && this.timer.passed();
    }

    protected void onTick(TickEvent.ClientTickEvent event) {
    }

    protected void onRender(RenderWorldLastEvent event) {
    }

    protected void onChat(String message) {
    }

    protected void onTablistUpdate(UpdateTablistEvent event) {
    }

    protected void onOverlayRender(RenderGameOverlayEvent event) {
    }

    protected void onPacketReceive(PacketEvent.Received event) {
    }

    protected void onWorldLoad(WorldEvent.Load event) {
    }

    protected void onWorldUnload(WorldEvent.Unload event) {
    }

    protected void onBlockChange(BlockChangeEvent event) {
    }

    protected void onBlockDestroy(BlockDestroyEvent event) {
    }

    protected void onKeyEvent(InputEvent.KeyInputEvent event) {
    }

    protected void log(String message) {
        Logger.sendLog(this.formatMessage(message));
    }

    protected void send(String message) {
        Logger.sendMessage(this.formatMessage(message));
    }

    protected void error(String message) {
        Logger.sendError(this.formatMessage(message));
    }

    protected void warn(String message) {
        Logger.sendWarning(this.formatMessage(message));
    }

    protected void note(String message) {
        Logger.sendNote(this.formatMessage(message));
    }

    protected String formatMessage(String message) {
        return "[" + this.getName() + "] " + message;
    }
}

