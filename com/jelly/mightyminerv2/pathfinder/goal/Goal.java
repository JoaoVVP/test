/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.goal;

import com.jelly.mightyminerv2.pathfinder.goal.IGoal;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ \u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0003H\u0016J \u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0003H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u0019"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "Lcom/jelly/mightyminerv2/pathfinder/goal/IGoal;", "goalX", "", "goalY", "goalZ", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "(IIILcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;)V", "SQRT_2", "", "getCtx", "()Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "getGoalX", "()I", "getGoalY", "getGoalZ", "heuristic", "x", "y", "z", "isAtGoal", "", "toString", "", "MightyMinerV2"})
public final class Goal
implements IGoal {
    private final int goalX;
    private final int goalY;
    private final int goalZ;
    @NotNull
    private final CalculationContext ctx;
    private final double SQRT_2;

    public Goal(int goalX, int goalY, int goalZ, @NotNull CalculationContext ctx) {
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        this.goalX = goalX;
        this.goalY = goalY;
        this.goalZ = goalZ;
        this.ctx = ctx;
        this.SQRT_2 = Math.sqrt(2.0);
    }

    public final int getGoalX() {
        return this.goalX;
    }

    public final int getGoalY() {
        return this.goalY;
    }

    public final int getGoalZ() {
        return this.goalZ;
    }

    @NotNull
    public final CalculationContext getCtx() {
        return this.ctx;
    }

    @Override
    public boolean isAtGoal(int x, int y, int z) {
        return this.goalX == x && this.goalY == y && this.goalZ == z;
    }

    @Override
    public double heuristic(int x, int y, int z) {
        int dx = Math.abs(this.goalX - x);
        int dz = Math.abs(this.goalZ - z);
        double straight = Math.abs(dx - dz);
        double vertical = Math.abs(this.goalY - y);
        double diagonal = Math.min(dx, dz);
        vertical = this.goalY > y ? (vertical *= 6.234399666206506) : (vertical *= this.ctx.getCost().getN_BLOCK_FALL_COST()[2] / 2.0);
        return (straight + diagonal * this.SQRT_2) * this.ctx.getCost().getONE_BLOCK_SPRINT_COST() + vertical;
    }

    @NotNull
    public String toString() {
        return "x: " + this.goalX + ", y: " + this.goalY + ", z: " + this.goalZ;
    }
}

