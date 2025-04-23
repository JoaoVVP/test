/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.Multithreading
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import cc.polyfrost.oneconfig.utils.Multithreading;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Nuker
extends AbstractFeature {
    private static final Nuker instance = new Nuker();
    private static final Minecraft mc = Minecraft.func_71410_x();
    private boolean enabled = false;
    private final int radius = 3;
    private boolean wasKeyPressed = false;
    private List<BlockPos> currentBlocksToBreak = new ArrayList<BlockPos>();
    private final AtomicBoolean isBreakingBlocks = new AtomicBoolean(false);

    public static Nuker getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "Nuker";
    }

    public void toggle() {
        this.enabled = !this.enabled;
        this.note("Nuker " + (this.enabled ? "enabled" : "disabled"));
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (MightyMinerConfig.nuker_toggle) {
            if (MightyMinerConfig.nuker_keyBind.isActive()) {
                if (!this.wasKeyPressed) {
                    this.toggle();
                    this.wasKeyPressed = true;
                }
            } else {
                this.wasKeyPressed = false;
            }
            if (!this.enabled || this.isBreakingBlocks.get()) {
                return;
            }
            Vec3 playerPosition = PlayerUtil.getPlayerEyePos();
            int playerX = (int)playerPosition.field_72450_a;
            int playerY = (int)playerPosition.field_72448_b;
            int playerZ = (int)playerPosition.field_72449_c;
            this.currentBlocksToBreak.clear();
            for (int x = playerX - 3; x <= playerX + 3; ++x) {
                for (int y = playerY - 3; y <= playerY + 3; ++y) {
                    for (int z = playerZ - 3; z <= playerZ + 3; ++z) {
                        BlockPos blockPos = new BlockPos(x, y, z);
                        Block block = Nuker.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
                        if (!block.func_149688_o().func_76220_a() || !(block.func_176195_g((World)Nuker.mc.field_71441_e, blockPos) >= 0.0f)) continue;
                        this.currentBlocksToBreak.add(blockPos);
                    }
                }
            }
            this.breakBlocksAsync();
        }
    }

    private void breakBlocksAsync() {
        this.isBreakingBlocks.set(true);
        Multithreading.runAsync(() -> {
            for (BlockPos pos : this.currentBlocksToBreak) {
                this.breakBlock(pos);
            }
            this.isBreakingBlocks.set(false);
        });
    }

    private void breakBlock(BlockPos pos) {
        if (Nuker.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_149688_o().func_76220_a()) {
            Nuker.mc.field_71439_g.func_71038_i();
            Nuker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
            Nuker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!this.enabled) {
            return;
        }
        for (BlockPos pos : this.currentBlocksToBreak) {
            RenderUtil.outlineBlock(pos, new Color(255, 0, 0, 128));
        }
        if (!this.currentBlocksToBreak.isEmpty()) {
            BlockPos firstBlock = this.currentBlocksToBreak.get(0);
            RenderUtil.outlineBlock(firstBlock, new Color(0, 255, 0, 128));
            RenderUtil.drawText("Mining: " + Nuker.mc.field_71441_e.func_180495_p(firstBlock).func_177230_c().func_149732_F(), (double)firstBlock.func_177958_n() + 0.5, (double)firstBlock.func_177956_o() + 1.2, (double)firstBlock.func_177952_p() + 0.5, 0.02f);
        }
    }

    public void onPlayerBreakBlock(BlockPos blockPos) {
        if (!this.enabled) {
            return;
        }
        ArrayList<BlockPos> nearbyBlocks = new ArrayList<BlockPos>();
        for (int x = blockPos.func_177958_n() - 3; x <= blockPos.func_177958_n() + 3; ++x) {
            for (int y = blockPos.func_177956_o() - 3; y <= blockPos.func_177956_o() + 3; ++y) {
                for (int z = blockPos.func_177952_p() - 3; z <= blockPos.func_177952_p() + 3; ++z) {
                    BlockPos nearbyBlockPos = new BlockPos(x, y, z);
                    Block block = Nuker.mc.field_71441_e.func_180495_p(nearbyBlockPos).func_177230_c();
                    if (!block.func_149688_o().func_76220_a() || !(block.func_176195_g((World)Nuker.mc.field_71441_e, nearbyBlockPos) >= 0.0f)) continue;
                    nearbyBlocks.add(nearbyBlockPos);
                }
            }
        }
        this.breakBlocksAsync(nearbyBlocks);
    }

    private void breakBlocksAsync(List<BlockPos> blocksToBreak) {
        Multithreading.runAsync(() -> {
            for (BlockPos pos : blocksToBreak) {
                this.breakBlock(pos);
            }
        });
    }
}

