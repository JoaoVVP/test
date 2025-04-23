/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockDestroyEvent
extends Event {
    private final BlockPos block;
    private final float progress;

    public BlockDestroyEvent(BlockPos block, float progress) {
        this.block = block;
        this.progress = progress;
    }

    public BlockPos getBlock() {
        return this.block;
    }

    public float getProgress() {
        return this.progress;
    }
}

