/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockPane
 *  net.minecraft.block.BlockStainedGlassPane
 *  net.minecraft.block.material.Material
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  org.spongepowered.asm.mixin.Mixin
 */
package com.jelly.mightyminerv2.mixin.client;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={BlockStainedGlassPane.class})
public abstract class MixinBlockStainedGlassPane
extends BlockPane {
    protected MixinBlockStainedGlassPane(Material materialIn, boolean canDrop) {
        super(materialIn, canDrop);
    }

    public void func_180654_a(IBlockAccess worldIn, BlockPos pos) {
        if (MightyMinerConfig.miscFullBlock) {
            this.func_149676_a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            super.func_180654_a(worldIn, pos);
        }
    }
}

