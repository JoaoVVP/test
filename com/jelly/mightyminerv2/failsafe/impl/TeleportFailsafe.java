/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.failsafe.FailsafeManager;
import com.jelly.mightyminerv2.feature.impl.LagDetector;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.helper.Clock;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TeleportFailsafe
extends AbstractFailsafe {
    private static final TeleportFailsafe instance = new TeleportFailsafe();
    private final LagDetector lagDetector = LagDetector.getInstance();
    private final Minecraft mc = Minecraft.func_71410_x();
    private final Clock deathCheckClock = new Clock();
    private boolean monitoringDeathMessages = false;

    public static TeleportFailsafe getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "TeleportFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.TELEPORT;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        if (!(event.packet instanceof S08PacketPlayerPosLook)) {
            return false;
        }
        this.startDeathMessageMonitoring();
        return true;
    }

    @Override
    public boolean react() {
        this.warn("Stopping macro due to teleport.");
        MacroManager.getInstance().disable();
        return true;
    }

    private void startDeathMessageMonitoring() {
        this.monitoringDeathMessages = true;
        this.deathCheckClock.schedule(3000L);
    }

    @SubscribeEvent
    public void handleClientTick(TickEvent.ClientTickEvent event) {
        if (this.monitoringDeathMessages && (this.deathCheckClock.passed() || this.isPlayerRecentlyDied())) {
            if (this.isPlayerRecentlyDied()) {
                this.warn("Death message detected, canceling teleport failsafe.");
                FailsafeManager.getInstance().removeFailsafeFromQueue(this);
            }
            this.monitoringDeathMessages = false;
        }
    }

    private boolean isPlayerRecentlyDied() {
        List chatMessages = this.mc.field_71456_v.func_146158_b().func_146238_c();
        for (int i = chatMessages.size() - 1; i >= Math.max(0, chatMessages.size() - 10); --i) {
            String message = ((String)chatMessages.get(i)).toLowerCase();
            if (!message.contains("fell into the void") && !message.contains("was knocked into the void by") && !message.contains("was slain by") && !message.contains("burned to death") && !message.contains("fell to death") && !message.contains("fell to their death with help from") && !message.contains("suffocated") && !message.contains("drowned") && !message.contains("was pricked to death by a cactus") && !message.contains("died")) continue;
            return true;
        }
        return false;
    }
}

