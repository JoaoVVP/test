/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.Slot
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.Clock;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ItemChangeFailsafe
extends AbstractFailsafe {
    private static final ItemChangeFailsafe instance = new ItemChangeFailsafe();
    private final Clock timer = new Clock();
    private final Map<String, Integer> removedItems = new HashMap<String, Integer>();

    public static ItemChangeFailsafe getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "ItemChangeFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.ITEM_CHANGE;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean onTick(TickEvent.ClientTickEvent event) {
        if (this.removedItems.isEmpty()) {
            if (this.timer.isScheduled()) {
                this.timer.reset();
            }
            return false;
        }
        if (!this.timer.isScheduled()) {
            this.timer.schedule(2000L);
        }
        if (this.timer.passed()) {
            this.log("timer passed");
        }
        return this.timer.passed();
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        Integer oldSlotNumber;
        if (this.mc.field_71462_r instanceof GuiContainer || !(event.packet instanceof S2FPacketSetSlot)) {
            return false;
        }
        S2FPacketSetSlot packet = (S2FPacketSetSlot)event.packet;
        int slot = packet.func_149173_d();
        if (slot <= 0) {
            return false;
        }
        if (slot >= 45) {
            return false;
        }
        Slot oldSlot = this.mc.field_71439_g.field_71069_bz.func_75139_a(slot);
        if (!oldSlot.func_75216_d() || !oldSlot.func_75211_c().func_82837_s()) {
            return false;
        }
        String oldItem = InventoryUtil.getItemId(oldSlot.func_75211_c());
        String newItem = InventoryUtil.getItemId(packet.func_149174_e());
        if (!oldItem.isEmpty() && newItem.isEmpty()) {
            String oldName = StringUtils.func_76338_a((String)oldSlot.func_75211_c().func_82833_r());
            if (MacroManager.getInstance().getCurrentMacro().getNecessaryItems().stream().anyMatch(oldName::contains)) {
                this.removedItems.put(oldItem, slot);
                this.note("Item " + oldName + " with id " + oldItem + " was removed from slot " + slot);
            }
        }
        if (!newItem.isEmpty() && oldItem.isEmpty() && (oldSlotNumber = this.removedItems.remove(newItem)) != null) {
            this.note("Item with id " + newItem + " was removed from " + oldSlotNumber + " and added back to slot " + slot);
            if (oldSlotNumber != slot) {
                this.warn("Item was moved.");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onChat(ClientChatReceivedEvent event) {
        if (event.type != 0 || this.removedItems.isEmpty()) {
            return false;
        }
        String message = event.message.func_150260_c();
        if (message.equals("Oh no! Your Pickonimbus 2000 broke!") && this.removedItems.remove("PICKONIMBUS") != null) {
            this.error("Pickonimbus broke. Ignoring");
        }
        return false;
    }

    @Override
    public boolean react() {
        MacroManager.getInstance().disable();
        Logger.sendWarning("Your item has been changed! Disabling macro.");
        return true;
    }

    @Override
    public void resetStates() {
        this.timer.reset();
        this.removedItems.clear();
    }
}

