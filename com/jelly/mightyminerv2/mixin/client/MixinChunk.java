/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.jelly.mightyminerv2.mixin.client;

import com.jelly.mightyminerv2.event.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Chunk.class})
public class MixinChunk {
    @Inject(method={"setBlockState"}, at={@At(value="RETURN")})
    public void setBlockState(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> cir) {
        IBlockState old = (IBlockState)cir.getReturnValue();
        if (old == null || state == old) {
            return;
        }
        MinecraftForge.EVENT_BUS.post((Event)new BlockChangeEvent(pos, old, state));
    }
}

