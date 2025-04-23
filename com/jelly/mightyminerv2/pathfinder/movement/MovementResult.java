/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.movement;

import kotlin.Metadata;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0015\u001a\u00020\u0016J\u0006\u0010\u0017\u001a\u00020\u0018J\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\nR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0012\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000e\u00a8\u0006\u001a"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "", "()V", "cost", "", "getCost", "()D", "setCost", "(D)V", "x", "", "getX", "()I", "setX", "(I)V", "y", "getY", "setY", "z", "getZ", "setZ", "getDest", "Lnet/minecraft/util/BlockPos;", "reset", "", "set", "MightyMinerV2"})
public final class MovementResult {
    private int x;
    private int y;
    private int z;
    private double cost = 1000000.0;

    public final int getX() {
        return this.x;
    }

    public final void setX(int n) {
        this.x = n;
    }

    public final int getY() {
        return this.y;
    }

    public final void setY(int n) {
        this.y = n;
    }

    public final int getZ() {
        return this.z;
    }

    public final void setZ(int n) {
        this.z = n;
    }

    public final double getCost() {
        return this.cost;
    }

    public final void setCost(double d) {
        this.cost = d;
    }

    public final void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.cost = 1000000.0;
    }

    @NotNull
    public final BlockPos getDest() {
        return new BlockPos(this.x, this.y, this.z);
    }
}

