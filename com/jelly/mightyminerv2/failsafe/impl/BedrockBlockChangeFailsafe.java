/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.server.S23PacketBlockChange
 *  net.minecraft.util.BlockPos
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.Clock;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BedrockBlockChangeFailsafe
extends AbstractFailsafe {
    private static final BedrockBlockChangeFailsafe instance = new BedrockBlockChangeFailsafe();
    private final Clock timer = new Clock();
    private final List<Long> bedrockChangeTimestamps = new ArrayList<Long>();
    private static final int THRESHOLD = 20;
    private static final long TIME_WINDOW = 100L;
    private static final int RADIUS = 10;

    public static BedrockBlockChangeFailsafe getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "BedrockBlockChangeFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.BLOCK_CHANGE;
    }

    @Override
    public int getPriority() {
        return 7;
    }

    @Override
    public boolean onTick(TickEvent.ClientTickEvent event) {
        long currentTime = System.currentTimeMillis();
        ArrayList<Long> validTimestamps = new ArrayList<Long>();
        for (Long timestamp : this.bedrockChangeTimestamps) {
            if (currentTime - timestamp > 100L) continue;
            validTimestamps.add(timestamp);
        }
        this.bedrockChangeTimestamps.clear();
        this.bedrockChangeTimestamps.addAll(validTimestamps);
        if (this.bedrockChangeTimestamps.size() >= 20) {
            Logger.sendWarning("Too many Bedrock block changes in the last 0.1 seconds. Triggering failsafe.");
            return true;
        }
        return false;
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        if (event.packet instanceof S23PacketBlockChange) {
            double radiusSquared;
            BlockPos playerPos;
            double distance;
            S23PacketBlockChange packet = (S23PacketBlockChange)event.packet;
            BlockPos blockPos = packet.func_179827_b();
            Block block = packet.func_180728_a().func_177230_c();
            if (block == Blocks.field_150357_h && (distance = (playerPos = this.mc.field_71439_g.func_180425_c()).func_177954_c((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p())) <= (radiusSquared = 100.0)) {
                long currentTime = System.currentTimeMillis();
                this.bedrockChangeTimestamps.add(currentTime);
            }
        }
        return false;
    }

    @Override
    public boolean react() {
        MacroManager.getInstance().disable();
        Logger.sendWarning("Too many Bedrock block changes nearby! Disabling macro.");
        return true;
    }

    @Override
    public void resetStates() {
        this.bedrockChangeTimestamps.clear();
        this.timer.reset();
    }
}

