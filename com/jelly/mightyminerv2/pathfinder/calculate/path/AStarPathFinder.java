/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.jelly.mightyminerv2.pathfinder.calculate.path;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.jelly.mightyminerv2.pathfinder.calculate.Path;
import com.jelly.mightyminerv2.pathfinder.calculate.PathNode;
import com.jelly.mightyminerv2.pathfinder.calculate.openset.BinaryHeapOpenSet;
import com.jelly.mightyminerv2.pathfinder.goal.Goal;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.MovementResult;
import com.jelly.mightyminerv2.pathfinder.movement.Moves;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J&\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020!R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015\u00a8\u0006\""}, d2={"Lcom/jelly/mightyminerv2/pathfinder/calculate/path/AStarPathFinder;", "", "startX", "", "startY", "startZ", "goal", "Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "(IIILcom/jelly/mightyminerv2/pathfinder/goal/Goal;Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;)V", "calculating", "", "closedSet", "Lcom/jelly/mightyminerv2/deps/it/unimi/dsi/fastutil/longs/Long2ObjectMap;", "Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "getCtx", "()Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "getGoal", "()Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "getStartX", "()I", "getStartY", "getStartZ", "calculatePath", "Lcom/jelly/mightyminerv2/pathfinder/calculate/Path;", "getNode", "x", "y", "z", "hash", "", "requestStop", "", "MightyMinerV2"})
public final class AStarPathFinder {
    private final int startX;
    private final int startY;
    private final int startZ;
    @NotNull
    private final Goal goal;
    @NotNull
    private final CalculationContext ctx;
    @NotNull
    private final Long2ObjectMap<PathNode> closedSet;
    private boolean calculating;

    public AStarPathFinder(int startX, int startY, int startZ, @NotNull Goal goal, @NotNull CalculationContext ctx) {
        Intrinsics.checkNotNullParameter((Object)goal, (String)"goal");
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.goal = goal;
        this.ctx = ctx;
        this.closedSet = new Long2ObjectOpenHashMap();
    }

    public final int getStartX() {
        return this.startX;
    }

    public final int getStartY() {
        return this.startY;
    }

    public final int getStartZ() {
        return this.startZ;
    }

    @NotNull
    public final Goal getGoal() {
        return this.goal;
    }

    @NotNull
    public final CalculationContext getCtx() {
        return this.ctx;
    }

    @Nullable
    public final Path calculatePath() {
        this.calculating = true;
        BinaryHeapOpenSet openSet = new BinaryHeapOpenSet(0, 1, null);
        PathNode startNode = new PathNode(this.startX, this.startY, this.startZ, this.goal);
        MovementResult res = new MovementResult();
        Moves[] moves = Moves.values();
        startNode.setCostSoFar(0.0);
        startNode.setTotalCost(startNode.getCostToEnd());
        openSet.add(startNode);
        while (!openSet.isEmpty() && this.calculating) {
            PathNode currentNode = openSet.poll();
            if (this.goal.isAtGoal(currentNode.getX(), currentNode.getY(), currentNode.getZ())) {
                return new Path(startNode, currentNode, this.goal, this.ctx);
            }
            for (Moves move : moves) {
                res.reset();
                move.calculate(this.ctx, currentNode.getX(), currentNode.getY(), currentNode.getZ(), res);
                double cost = res.getCost();
                if (cost >= this.ctx.getCost().getINF_COST()) continue;
                PathNode neighbourNode = this.getNode(res.getX(), res.getY(), res.getZ(), PathNode.Companion.longHash(res.getX(), res.getY(), res.getZ()));
                double neighbourCostSoFar = currentNode.getCostSoFar() + cost;
                if (!(neighbourNode.getCostSoFar() > neighbourCostSoFar)) continue;
                neighbourNode.setParentNode(currentNode);
                neighbourNode.setCostSoFar(neighbourCostSoFar);
                neighbourNode.setTotalCost(neighbourCostSoFar + neighbourNode.getCostToEnd());
                if (neighbourNode.getHeapPosition() == -1) {
                    openSet.add(neighbourNode);
                    continue;
                }
                openSet.relocate(neighbourNode);
            }
        }
        this.calculating = false;
        return null;
    }

    @NotNull
    public final PathNode getNode(int x, int y, int z, long hash) {
        PathNode n = (PathNode)this.closedSet.get(hash);
        if (n == null) {
            n = new PathNode(x, y, z, this.goal);
            this.closedSet.put(hash, n);
        }
        return n;
    }

    public final void requestStop() {
        if (!this.calculating) {
            return;
        }
        this.calculating = false;
    }
}

