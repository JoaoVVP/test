/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.jelly.mightyminerv2.pathfinder.calculate;

import com.jelly.mightyminerv2.pathfinder.goal.Goal;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u001d\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 /2\u00020\u0001:\u0001/B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0013\u0010'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\u0006\u0010*\u001a\u00020+J\b\u0010,\u001a\u00020\u0003H\u0016J\b\u0010-\u001a\u00020.H\u0016R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0000X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\f\"\u0004\b \u0010\u000eR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0016\"\u0004\b\"\u0010\u0018R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0016\"\u0004\b$\u0010\u0018R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0016\"\u0004\b&\u0010\u0018\u00a8\u00060"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "", "x", "", "y", "z", "goal", "Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "(IIILcom/jelly/mightyminerv2/pathfinder/goal/Goal;)V", "costSoFar", "", "getCostSoFar", "()D", "setCostSoFar", "(D)V", "costToEnd", "getCostToEnd", "setCostToEnd", "getGoal", "()Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "heapPosition", "getHeapPosition", "()I", "setHeapPosition", "(I)V", "parentNode", "getParentNode", "()Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "setParentNode", "(Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;)V", "totalCost", "getTotalCost", "setTotalCost", "getX", "setX", "getY", "setY", "getZ", "setZ", "equals", "", "other", "getBlock", "Lnet/minecraft/util/BlockPos;", "hashCode", "toString", "", "Companion", "MightyMinerV2"})
public final class PathNode {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private int x;
    private int y;
    private int z;
    @NotNull
    private final Goal goal;
    private double costSoFar;
    private double costToEnd;
    private double totalCost;
    private int heapPosition;
    @Nullable
    private PathNode parentNode;

    public PathNode(int x, int y, int z, @NotNull Goal goal) {
        Intrinsics.checkNotNullParameter((Object)goal, (String)"goal");
        this.x = x;
        this.y = y;
        this.z = z;
        this.goal = goal;
        this.costSoFar = 1000000.0;
        this.costToEnd = this.goal.heuristic(this.x, this.y, this.z);
        this.totalCost = 1.0;
        this.heapPosition = -1;
    }

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

    @NotNull
    public final Goal getGoal() {
        return this.goal;
    }

    public final double getCostSoFar() {
        return this.costSoFar;
    }

    public final void setCostSoFar(double d) {
        this.costSoFar = d;
    }

    public final double getCostToEnd() {
        return this.costToEnd;
    }

    public final void setCostToEnd(double d) {
        this.costToEnd = d;
    }

    public final double getTotalCost() {
        return this.totalCost;
    }

    public final void setTotalCost(double d) {
        this.totalCost = d;
    }

    public final int getHeapPosition() {
        return this.heapPosition;
    }

    public final void setHeapPosition(int n) {
        this.heapPosition = n;
    }

    @Nullable
    public final PathNode getParentNode() {
        return this.parentNode;
    }

    public final void setParentNode(@Nullable PathNode pathNode) {
        this.parentNode = pathNode;
    }

    public boolean equals(@Nullable Object other) {
        Intrinsics.checkNotNull((Object)other, (String)"null cannot be cast to non-null type com.jelly.mightyminerv2.pathfinder.calculate.PathNode");
        PathNode otter = (PathNode)other;
        return otter.x == this.x && otter.y == this.y && otter.z == this.z;
    }

    public int hashCode() {
        return (int)Companion.longHash(this.x, this.y, this.z);
    }

    @NotNull
    public final BlockPos getBlock() {
        return new BlockPos(this.x, this.y, this.z);
    }

    @NotNull
    public String toString() {
        return "PathNode(x: " + this.x + ", y: " + this.y + ", z: " + this.z + ", costSoFar: " + this.costSoFar + ", costToEnd: " + this.costToEnd + " totalCost: " + this.totalCost + ')';
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006\u00a8\u0006\t"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode$Companion;", "", "()V", "longHash", "", "x", "", "y", "z", "MightyMinerV2"})
    public static final class Companion {
        private Companion() {
        }

        public final long longHash(int x, int y, int z) {
            long hash = 3241L;
            hash = 3457689L * hash + (long)x;
            hash = 8734625L * hash + (long)y;
            hash = 2873465L * hash + (long)z;
            return hash;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

