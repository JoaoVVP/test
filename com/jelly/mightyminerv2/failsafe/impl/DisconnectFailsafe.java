/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiDisconnected
 *  net.minecraft.network.play.server.S40PacketDisconnect
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.network.play.server.S40PacketDisconnect;

public class DisconnectFailsafe
extends AbstractFailsafe {
    private static final DisconnectFailsafe instance = new DisconnectFailsafe();

    public static DisconnectFailsafe getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "DisconnectFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.DISCONNECT;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        return event.packet instanceof S40PacketDisconnect || this.mc.field_71462_r instanceof GuiDisconnected;
    }

    @Override
    public boolean react() {
        this.warn("Disconnected. Disabling Macro");
        MacroManager.getInstance().disable();
        return true;
    }
}

