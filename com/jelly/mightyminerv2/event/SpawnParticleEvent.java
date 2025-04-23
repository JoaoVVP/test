/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SpawnParticleEvent
extends Event {
    EnumParticleTypes particleTypes;
    boolean isLongDistance;
    double xCoord;
    double yCoord;
    double zCoord;
    double xOffset;
    double yOffset;
    double zOffset;
    int[] params;

    public SpawnParticleEvent(EnumParticleTypes particleTypes, boolean isLongDistance, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int[] params) {
        this.particleTypes = particleTypes;
        this.isLongDistance = isLongDistance;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.params = params;
    }

    public Vec3 getPos() {
        return new Vec3(this.xCoord, this.yCoord, this.zCoord);
    }

    public EnumParticleTypes getParticleTypes() {
        return this.particleTypes;
    }

    public boolean isLongDistance() {
        return this.isLongDistance;
    }

    public double getXCoord() {
        return this.xCoord;
    }

    public double getYCoord() {
        return this.yCoord;
    }

    public double getZCoord() {
        return this.zCoord;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public double getZOffset() {
        return this.zOffset;
    }

    public int[] getParams() {
        return this.params;
    }
}

