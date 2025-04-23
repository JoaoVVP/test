/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.costs;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0013\n\u0002\b \u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010&\u001a\u00020\u00032\u0006\u0010'\u001a\u00020\u0003H\u0002J\u0010\u0010(\u001a\u00020\u00032\u0006\u0010)\u001a\u00020\u0007H\u0002J\u000e\u0010*\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u0003J\b\u0010,\u001a\u00020\u0011H\u0002J\u0010\u0010-\u001a\u00020\u00032\u0006\u0010.\u001a\u00020\u0003H\u0002J\u0010\u0010/\u001a\u00020\u00032\u0006\u0010.\u001a\u00020\u0003H\u0002J\u000e\u00100\u001a\u00020\u00032\u0006\u0010)\u001a\u00020\u0007R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\u0003X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000bR\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000bR\u0011\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000bR\u0011\u0010\u001a\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u000bR\u0011\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u000bR\u0014\u0010\u001e\u001a\u00020\u0003X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u000bR\u0014\u0010 \u001a\u00020\u0003X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u000bR\u0011\u0010\"\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u000bR\u0011\u0010$\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u000b\u00a8\u00061"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/costs/ActionCosts;", "", "SPRINT_MOVEMENT_FACTOR", "", "WALKING_MOVEMENT_FACTOR", "SNEAKING_MOVEMENT_FACTOR", "JUMP_BOOST_LEVEL", "", "(DDDI)V", "CENTER_AFTER_FALL_COST", "getCENTER_AFTER_FALL_COST", "()D", "INF_COST", "getINF_COST", "JUMP_ONE_BLOCK_COST", "getJUMP_ONE_BLOCK_COST", "N_BLOCK_FALL_COST", "", "getN_BLOCK_FALL_COST", "()[D", "ONE_BLOCK_SNEAK_COST", "getONE_BLOCK_SNEAK_COST", "ONE_BLOCK_SPRINT_COST", "getONE_BLOCK_SPRINT_COST", "ONE_BLOCK_WALK_COST", "getONE_BLOCK_WALK_COST", "ONE_BLOCK_WALK_IN_WATER_COST", "getONE_BLOCK_WALK_IN_WATER_COST", "ONE_BLOCK_WALK_OVER_SOUL_SAND_COST", "getONE_BLOCK_WALK_OVER_SOUL_SAND_COST", "ONE_DOWN_LADDER_COST", "getONE_DOWN_LADDER_COST", "ONE_UP_LADDER_COST", "getONE_UP_LADDER_COST", "SPRINT_MULTIPLIER", "getSPRINT_MULTIPLIER", "WALK_OFF_ONE_BLOCK_COST", "getWALK_OFF_ONE_BLOCK_COST", "actionTime", "friction", "downwardMotionAtTick", "tick", "fallDistanceToTicks", "distance", "generateNBlocksFallCost", "getWalkingFriction", "landMovementFactor", "getWalkingInWaterFriction", "motionYAtTick", "MightyMinerV2"})
public final class ActionCosts {
    private final double INF_COST;
    @NotNull
    private final double[] N_BLOCK_FALL_COST;
    private final double ONE_UP_LADDER_COST;
    private final double ONE_DOWN_LADDER_COST;
    private final double JUMP_ONE_BLOCK_COST;
    private final double ONE_BLOCK_WALK_COST;
    private final double ONE_BLOCK_SPRINT_COST;
    private final double ONE_BLOCK_SNEAK_COST;
    private final double ONE_BLOCK_WALK_IN_WATER_COST;
    private final double ONE_BLOCK_WALK_OVER_SOUL_SAND_COST;
    private final double WALK_OFF_ONE_BLOCK_COST;
    private final double CENTER_AFTER_FALL_COST;
    private final double SPRINT_MULTIPLIER;

    public ActionCosts(double SPRINT_MOVEMENT_FACTOR, double WALKING_MOVEMENT_FACTOR, double SNEAKING_MOVEMENT_FACTOR, int JUMP_BOOST_LEVEL) {
        this.INF_COST = 1000000.0;
        this.N_BLOCK_FALL_COST = this.generateNBlocksFallCost();
        this.ONE_UP_LADDER_COST = 0.8503401360544218;
        this.ONE_DOWN_LADDER_COST = 6.666666666666667;
        this.ONE_BLOCK_WALK_COST = 1.0 / this.actionTime(this.getWalkingFriction(WALKING_MOVEMENT_FACTOR));
        this.ONE_BLOCK_SPRINT_COST = 1.0 / this.actionTime(this.getWalkingFriction(SPRINT_MOVEMENT_FACTOR));
        this.ONE_BLOCK_SNEAK_COST = 1.0 / this.actionTime(this.getWalkingFriction(SNEAKING_MOVEMENT_FACTOR));
        this.ONE_BLOCK_WALK_IN_WATER_COST = (double)20 * this.actionTime(this.getWalkingInWaterFriction(WALKING_MOVEMENT_FACTOR));
        this.ONE_BLOCK_WALK_OVER_SOUL_SAND_COST = this.ONE_BLOCK_WALK_COST * (double)2;
        this.WALK_OFF_ONE_BLOCK_COST = this.ONE_BLOCK_WALK_COST * 0.8;
        this.CENTER_AFTER_FALL_COST = this.ONE_BLOCK_WALK_COST * 0.2;
        this.SPRINT_MULTIPLIER = WALKING_MOVEMENT_FACTOR / SPRINT_MOVEMENT_FACTOR;
        double vel = 0.42 + (double)(JUMP_BOOST_LEVEL + 1) * 0.1;
        double height = 0.0;
        double time = 1.0;
        for (int i = 1; i < 21; ++i) {
            height += vel;
            if ((vel = (vel - 0.08) * 0.98) < 0.0) break;
            double d = time;
            time = d + 1.0;
        }
        this.JUMP_ONE_BLOCK_COST = time + this.fallDistanceToTicks(height - 1.0);
    }

    public /* synthetic */ ActionCosts(double d, double d2, double d3, int n, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 1) != 0) {
            d = 0.13;
        }
        if ((n2 & 2) != 0) {
            d2 = 0.1;
        }
        if ((n2 & 4) != 0) {
            d3 = 0.03;
        }
        if ((n2 & 8) != 0) {
            n = -1;
        }
        this(d, d2, d3, n);
    }

    public final double getINF_COST() {
        return this.INF_COST;
    }

    @NotNull
    public final double[] getN_BLOCK_FALL_COST() {
        return this.N_BLOCK_FALL_COST;
    }

    public final double getONE_UP_LADDER_COST() {
        return this.ONE_UP_LADDER_COST;
    }

    public final double getONE_DOWN_LADDER_COST() {
        return this.ONE_DOWN_LADDER_COST;
    }

    public final double getJUMP_ONE_BLOCK_COST() {
        return this.JUMP_ONE_BLOCK_COST;
    }

    public final double getONE_BLOCK_WALK_COST() {
        return this.ONE_BLOCK_WALK_COST;
    }

    public final double getONE_BLOCK_SPRINT_COST() {
        return this.ONE_BLOCK_SPRINT_COST;
    }

    public final double getONE_BLOCK_SNEAK_COST() {
        return this.ONE_BLOCK_SNEAK_COST;
    }

    public final double getONE_BLOCK_WALK_IN_WATER_COST() {
        return this.ONE_BLOCK_WALK_IN_WATER_COST;
    }

    public final double getONE_BLOCK_WALK_OVER_SOUL_SAND_COST() {
        return this.ONE_BLOCK_WALK_OVER_SOUL_SAND_COST;
    }

    public final double getWALK_OFF_ONE_BLOCK_COST() {
        return this.WALK_OFF_ONE_BLOCK_COST;
    }

    public final double getCENTER_AFTER_FALL_COST() {
        return this.CENTER_AFTER_FALL_COST;
    }

    public final double getSPRINT_MULTIPLIER() {
        return this.SPRINT_MULTIPLIER;
    }

    private final double getWalkingFriction(double landMovementFactor) {
        return landMovementFactor * 0.2160000318483593;
    }

    private final double getWalkingInWaterFriction(double landMovementFactor) {
        return 0.02 + (landMovementFactor - 0.02) * 0.3333333333333333;
    }

    private final double actionTime(double friction) {
        return friction * (double)10;
    }

    public final double motionYAtTick(int tick) {
        double velocity = -0.0784000015258789;
        int i = 1;
        if (i <= tick) {
            while (true) {
                velocity = (velocity - 0.08) * (double)0.98f;
                if (i == tick) break;
                ++i;
            }
        }
        return velocity;
    }

    public final double fallDistanceToTicks(double distance) {
        if (distance == 0.0) {
            return 0.0;
        }
        double tmpDistance = distance;
        int tickCount = 0;
        double fallDistance;
        while (!(tmpDistance <= (fallDistance = this.downwardMotionAtTick(tickCount)))) {
            tmpDistance -= fallDistance;
            ++tickCount;
        }
        return (double)tickCount + tmpDistance / fallDistance;
    }

    private final double downwardMotionAtTick(int tick) {
        return (Math.pow(0.98, tick) - 1.0) * -3.92;
    }

    private final double[] generateNBlocksFallCost() {
        double[] timeCost = new double[257];
        double currentDistance = 0.0;
        int targetDistance = 1;
        int tickCount = 0;
        while (true) {
            double velocityAtTick;
            if (currentDistance + (velocityAtTick = this.downwardMotionAtTick(tickCount)) >= (double)targetDistance) {
                timeCost[targetDistance] = (double)tickCount + ((double)targetDistance - currentDistance) / velocityAtTick;
                if (++targetDistance > 256) break;
                continue;
            }
            currentDistance += velocityAtTick;
            ++tickCount;
        }
        return timeCost;
    }

    public ActionCosts() {
        this(0.0, 0.0, 0.0, 0, 15, null);
    }
}

