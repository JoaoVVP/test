/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.S03PacketTimeUpdate
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.util.helper.Clock;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LagDetector
extends AbstractFeature {
    private static LagDetector instance;
    private final Clock lagTimer = new Clock();
    private long lastReceivedPacketTime = -1L;

    public static LagDetector getInstance() {
        if (instance == null) {
            instance = new LagDetector();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "LagDetector";
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent event) {
        if (event.packet instanceof S03PacketTimeUpdate) {
            this.lastReceivedPacketTime = System.currentTimeMillis();
        }
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - this.lastReceivedPacketTime > 1300L) {
            this.lagTimer.schedule(900L);
        }
        if (this.lagTimer.isScheduled() && this.lagTimer.passed()) {
            this.lagTimer.reset();
        }
    }

    public boolean isLagging() {
        return this.lagTimer.isScheduled();
    }

    private float getTimeSinceLastPacket() {
        return (float)(System.currentTimeMillis() - this.lastReceivedPacketTime) / 1000.0f;
    }
}

