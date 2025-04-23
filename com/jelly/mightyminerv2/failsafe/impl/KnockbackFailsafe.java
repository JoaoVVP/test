/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

public class KnockbackFailsafe
extends AbstractFailsafe {
    private static final KnockbackFailsafe instance = new KnockbackFailsafe();
    private Vec3 lastPlayerPos = null;

    public static KnockbackFailsafe getInstance() {
        return instance;
    }

    @Override
    public int getPriority() {
        return 8;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.KNOCKBACK;
    }

    public boolean check() {
        double knockbackThreshold;
        double deltaZ;
        double deltaX;
        double knockbackDistance;
        Vec3 currentPlayerPos = Minecraft.func_71410_x().field_71439_g.func_174791_d();
        if (this.lastPlayerPos != null && (knockbackDistance = Math.sqrt((deltaX = currentPlayerPos.field_72450_a - this.lastPlayerPos.field_72450_a) * deltaX + (deltaZ = currentPlayerPos.field_72449_c - this.lastPlayerPos.field_72449_c) * deltaZ)) > (knockbackThreshold = 0.4)) {
            return true;
        }
        this.lastPlayerPos = currentPlayerPos;
        return false;
    }

    @Override
    public boolean react() {
        MacroManager.getInstance().disable();
        Logger.sendWarning("Knockback has been detected! Disabeling macro.");
        return true;
    }
}

