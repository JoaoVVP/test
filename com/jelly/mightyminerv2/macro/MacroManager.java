/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.macro;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.event.UpdateTablistEvent;
import com.jelly.mightyminerv2.feature.FeatureManager;
import com.jelly.mightyminerv2.macro.AbstractMacro;
import com.jelly.mightyminerv2.macro.commissionmacro.CommissionMacro;
import com.jelly.mightyminerv2.macro.mithrilmacro.MithrilMacro;
import com.jelly.mightyminerv2.util.Logger;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MacroManager {
    private static MacroManager instance = new MacroManager();
    private AbstractMacro currentMacro;

    public static MacroManager getInstance() {
        return instance;
    }

    public AbstractMacro getCurrentMacro() {
        switch (MightyMinerConfig.macroType) {
            case 0: {
                return CommissionMacro.getInstance();
            }
            case 1: {
                return MithrilMacro.getInstance();
            }
        }
        return CommissionMacro.getInstance();
    }

    public void toggle() {
        this.log("Toggling");
        if (this.currentMacro != null) {
            this.log("CurrMacro != null");
            this.disable();
        } else {
            this.log("CurrMacro == null");
            this.enable();
        }
    }

    public void enable() {
        this.log("Macro::enable");
        FeatureManager.getInstance().enableAll();
        this.currentMacro = this.getCurrentMacro();
        this.currentMacro.enable();
        this.send(this.currentMacro.getName() + " Enabled");
    }

    public void disable() {
        if (this.currentMacro == null) {
            return;
        }
        this.log("Macro::disable");
        FeatureManager.getInstance().disableAll();
        this.currentMacro.disable();
        this.send(this.currentMacro.getName() + " Disabled");
        this.currentMacro = null;
    }

    public void pause() {
        if (this.currentMacro == null) {
            return;
        }
        this.log("Macro::pause");
        this.currentMacro.pause();
        this.send(this.currentMacro.getName() + " Paused");
    }

    public void resume() {
        if (this.currentMacro == null) {
            return;
        }
        this.log("Macro::resume");
        this.currentMacro.resume();
        this.send(this.currentMacro.getName() + " Resumed");
    }

    public boolean isEnabled() {
        return this.currentMacro != null;
    }

    public boolean isRunning() {
        return this.currentMacro != null && this.currentMacro.isEnabled();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.currentMacro == null) {
            return;
        }
        if (!this.currentMacro.isEnabled()) {
            this.disable();
            return;
        }
        this.currentMacro.onTick(event);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (this.currentMacro == null) {
            return;
        }
        this.currentMacro.onChat(event.message.func_150260_c());
    }

    @SubscribeEvent
    public void onTablistUpdate(UpdateTablistEvent event) {
        if (this.currentMacro == null) {
            return;
        }
        this.currentMacro.onTablistUpdate(event);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (this.currentMacro == null) {
            return;
        }
        this.currentMacro.onWorldRender(event);
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (this.currentMacro == null) {
            return;
        }
        this.currentMacro.onOverlayRender(event);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Received event) {
        if (this.currentMacro == null) {
            return;
        }
        this.currentMacro.onReceivePacket(event);
    }

    public void log(String message) {
        Logger.sendLog(this.getMessage(message));
    }

    public void send(String message) {
        Logger.sendMessage(this.getMessage(message));
    }

    public void error(String message) {
        Logger.sendError(this.getMessage(message));
    }

    public void warn(String message) {
        Logger.sendWarning(this.getMessage(message));
    }

    public String getMessage(String message) {
        return "[MacroHandler] " + message;
    }
}

