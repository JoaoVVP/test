/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.Logger;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class RotationFailsafe
extends AbstractFailsafe {
    private static final RotationFailsafe instance = new RotationFailsafe();

    public static RotationFailsafe getInstance() {
        return instance;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.ROTATION;
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        if (!(event.packet instanceof S08PacketPlayerPosLook)) {
            return false;
        }
        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.packet;
        float playerYaw = this.mc.field_71439_g.field_70177_z;
        float playerPitch = this.mc.field_71439_g.field_70125_A;
        float packetYaw = packet.func_148931_f();
        float packetPitch = packet.func_148930_g();
        float yawDifference = Math.abs(playerYaw - packetYaw);
        float pitchDifference = Math.abs(playerPitch - packetPitch);
        double yawThreshold = 30.0;
        double pitchThreshold = 30.0;
        return (double)yawDifference > yawThreshold || (double)pitchDifference > pitchThreshold;
    }

    @Override
    public boolean react() {
        MacroManager.getInstance().disable();
        Logger.sendWarning("You`ve got rotated! Disabeling macro.");
        return true;
    }
}

