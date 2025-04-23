/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.calculate;

import com.jelly.mightyminerv2.pathfinder.calculate.Path;
import com.jelly.mightyminerv2.pathfinder.calculate.PathNode;
import com.jelly.mightyminerv2.pathfinder.goal.Goal;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.util.BlockUtil;
import com.jelly.mightyminerv2.pathfinder.util.ExtensionKt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\u000f\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\f0\u0014J\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\f0\u00142\u0006\u0010\u0004\u001a\u00020\u0003R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0004\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R \u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R \u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\f0\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0016\"\u0004\b\u001b\u0010\u0018R \u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\f0\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0016\"\u0004\b\u001e\u0010\u0018R\u001a\u0010\u0002\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u000e\"\u0004\b \u0010\u0010\u00a8\u0006#"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/calculate/Path;", "", "start", "Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "end", "goal", "Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "(Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;)V", "getCtx", "()Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "Lnet/minecraft/util/BlockPos;", "getEnd", "()Lnet/minecraft/util/BlockPos;", "setEnd", "(Lnet/minecraft/util/BlockPos;)V", "getGoal", "()Lcom/jelly/mightyminerv2/pathfinder/goal/Goal;", "node", "", "getNode", "()Ljava/util/List;", "setNode", "(Ljava/util/List;)V", "path", "getPath", "setPath", "smoothPath", "getSmoothPath", "setSmoothPath", "getStart", "setStart", "getSmoothedPath", "reconstructPath", "MightyMinerV2"})
public final class Path {
    @NotNull
    private final Goal goal;
    @NotNull
    private final CalculationContext ctx;
    @NotNull
    private BlockPos start;
    @NotNull
    private BlockPos end;
    @NotNull
    private List<? extends BlockPos> path;
    @NotNull
    private List<PathNode> node;
    @NotNull
    private List<? extends BlockPos> smoothPath;

    public Path(@NotNull PathNode start2, @NotNull PathNode end, @NotNull Goal goal, @NotNull CalculationContext ctx) {
        Intrinsics.checkNotNullParameter((Object)start2, (String)"start");
        Intrinsics.checkNotNullParameter((Object)end, (String)"end");
        Intrinsics.checkNotNullParameter((Object)goal, (String)"goal");
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        this.goal = goal;
        this.ctx = ctx;
        this.start = new BlockPos(start2.getX(), start2.getY(), start2.getZ());
        this.end = new BlockPos(end.getX(), end.getY(), end.getZ());
        this.smoothPath = CollectionsKt.emptyList();
        LinkedList<BlockPos> listOfBlocks = new LinkedList<BlockPos>();
        LinkedList<PathNode> listOfNodes = new LinkedList<PathNode>();
        for (PathNode temp = end; temp != null; temp = temp.getParentNode()) {
            listOfNodes.addFirst(temp);
            listOfBlocks.addFirst(new BlockPos(temp.getX(), temp.getY(), temp.getZ()));
        }
        this.path = CollectionsKt.toList((Iterable)listOfBlocks);
        this.node = CollectionsKt.toList((Iterable)listOfNodes);
    }

    @NotNull
    public final Goal getGoal() {
        return this.goal;
    }

    @NotNull
    public final CalculationContext getCtx() {
        return this.ctx;
    }

    @NotNull
    public final BlockPos getStart() {
        return this.start;
    }

    public final void setStart(@NotNull BlockPos blockPos) {
        Intrinsics.checkNotNullParameter((Object)blockPos, (String)"<set-?>");
        this.start = blockPos;
    }

    @NotNull
    public final BlockPos getEnd() {
        return this.end;
    }

    public final void setEnd(@NotNull BlockPos blockPos) {
        Intrinsics.checkNotNullParameter((Object)blockPos, (String)"<set-?>");
        this.end = blockPos;
    }

    @NotNull
    public final List<BlockPos> getPath() {
        return this.path;
    }

    public final void setPath(@NotNull List<? extends BlockPos> list) {
        Intrinsics.checkNotNullParameter(list, (String)"<set-?>");
        this.path = list;
    }

    @NotNull
    public final List<PathNode> getNode() {
        return this.node;
    }

    public final void setNode(@NotNull List<PathNode> list) {
        Intrinsics.checkNotNullParameter(list, (String)"<set-?>");
        this.node = list;
    }

    @NotNull
    public final List<BlockPos> getSmoothPath() {
        return this.smoothPath;
    }

    public final void setSmoothPath(@NotNull List<? extends BlockPos> list) {
        Intrinsics.checkNotNullParameter(list, (String)"<set-?>");
        this.smoothPath = list;
    }

    @NotNull
    public final List<BlockPos> getSmoothedPath() {
        if (!((Collection)this.smoothPath).isEmpty()) {
            return this.smoothPath;
        }
        LinkedList<BlockPos> smooth = new LinkedList<BlockPos>();
        if (!((Collection)this.path).isEmpty()) {
            smooth.add(this.path.get(0));
            int currPoint = 0;
            while (currPoint + 1 < this.path.size()) {
                int nextPos = currPoint + 1;
                int n = nextPos;
                int i = this.path.size() - 1;
                if (n <= i) {
                    while (true) {
                        if (BlockUtil.INSTANCE.bresenham(this.ctx, this.path.get(currPoint), this.path.get(i))) {
                            nextPos = i;
                            break;
                        }
                        if (i == n) break;
                        --i;
                    }
                }
                smooth.add(this.path.get(nextPos));
                currPoint = nextPos;
            }
        }
        this.smoothPath = CollectionsKt.toList((Iterable)smooth);
        return this.smoothPath;
    }

    @NotNull
    public final List<BlockPos> reconstructPath(@NotNull PathNode end) {
        Intrinsics.checkNotNullParameter((Object)end, (String)"end");
        List path = new ArrayList();
        for (PathNode currentNode = end; currentNode != null; currentNode = currentNode.getParentNode()) {
            path.add(0, currentNode.getBlock());
        }
        List smooth = new ArrayList();
        if (!((Collection)path).isEmpty()) {
            smooth.add(path.get(0));
            int currPoint = 0;
            int maxiters = 2000;
            while (currPoint + 1 < path.size() && maxiters-- > 0) {
                int nextPos = currPoint + 1;
                int n = nextPos;
                int i = path.size() - 1;
                if (n <= i) {
                    while (true) {
                        if (BlockUtil.INSTANCE.bresenham(this.ctx, ExtensionKt.toVec3((BlockPos)path.get(currPoint)), ExtensionKt.toVec3((BlockPos)path.get(i)))) {
                            nextPos = i;
                            break;
                        }
                        if (i == n) break;
                        --i;
                    }
                }
                smooth.add(path.get(nextPos));
                currPoint = nextPos;
            }
        }
        smooth.removeIf(arg_0 -> Path.reconstructPath$lambda$0(reconstructPath.1.INSTANCE, arg_0));
        return smooth;
    }

    private static final boolean reconstructPath$lambda$0(Function1 $tmp0, Object p0) {
        Intrinsics.checkNotNullParameter((Object)$tmp0, (String)"$tmp0");
        return (Boolean)$tmp0.invoke(p0);
    }
}

