/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.play.server.S09PacketHeldItemChange
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.Clock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SlotChangeFailsafe
extends AbstractFailsafe {
    private static final SlotChangeFailsafe instance = new SlotChangeFailsafe();
    private final Minecraft mc = Minecraft.func_71410_x();
    private final Clock timer = new Clock();
    private int lastSelectedSlot;
    private boolean slotChanged = false;

    public static SlotChangeFailsafe getInstance() {
        return instance;
    }

    private SlotChangeFailsafe() {
        this.lastSelectedSlot = this.mc.field_71439_g != null ? this.mc.field_71439_g.field_71071_by.field_70461_c : -1;
    }

    @Override
    public String getName() {
        return "SlotChangeFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.SLOT_CHANGE;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean onTick(TickEvent.ClientTickEvent event) {
        if (this.slotChanged && this.timer.passed()) {
            Logger.sendLog("Timer passed after slot change");
            return true;
        }
        return false;
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        S09PacketHeldItemChange packet;
        int slotIndex;
        if (event.packet instanceof S09PacketHeldItemChange && (slotIndex = (packet = (S09PacketHeldItemChange)event.packet).func_149385_c()) != this.lastSelectedSlot) {
            this.log("Slot changed by S09 packet from " + this.lastSelectedSlot + " to " + slotIndex);
            this.slotChanged = true;
            this.lastSelectedSlot = slotIndex;
            if (!this.timer.isScheduled()) {
                this.timer.schedule(2000L);
            }
        }
        return false;
    }

    @Override
    public boolean react() {
        if (this.slotChanged) {
            MacroManager.getInstance().disable();
            this.warn("Slot selection changed! Disabling macro.");
            this.slotChanged = false;
            return true;
        }
        return false;
    }

    @Override
    public void resetStates() {
        this.timer.reset();
        this.slotChanged = false;
        this.log("SlotChangeFailsafe state reset.");
    }
}

