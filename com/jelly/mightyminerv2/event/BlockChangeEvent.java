/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockChangeEvent
extends Event {
    public BlockPos pos;
    public IBlockState old;
    public IBlockState update;

    public BlockChangeEvent(BlockPos pos, IBlockState old, IBlockState update) {
        this.pos = pos;
        this.old = old;
        this.update = update;
    }
}

