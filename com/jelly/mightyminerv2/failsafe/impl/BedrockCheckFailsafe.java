/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class BedrockCheckFailsafe
extends AbstractFailsafe {
    private static final BedrockCheckFailsafe instance = new BedrockCheckFailsafe();
    private static final int CHECK_RADIUS = 5;
    private static final int BEDROCK_THRESHOLD = 10;

    public static BedrockCheckFailsafe getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "BedrockCheckFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.BEDROCK_CHECK;
    }

    @Override
    public int getPriority() {
        return 6;
    }

    public boolean checkForBedrock(Vec3 playerPos) {
        int bedrockCount = 0;
        for (int x = -5; x <= 5; ++x) {
            for (int y = -5; y <= 5; ++y) {
                for (int z = -5; z <= 5; ++z) {
                    BlockPos blockPos = new BlockPos(playerPos.field_72450_a + (double)x, playerPos.field_72448_b + (double)y, playerPos.field_72449_c + (double)z);
                    Block block = this.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
                    if (block == Blocks.field_150357_h) {
                        ++bedrockCount;
                    }
                    if (bedrockCount < 10) continue;
                    this.warn("BedrockCheckFailsafe triggered: found " + bedrockCount + " bedrock blocks.");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean react() {
        MacroManager.getInstance().disable();
        this.warn("Disabling macro due to bedrock surroundings.");
        return true;
    }
}

