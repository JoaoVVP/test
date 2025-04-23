/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.MightyMiner;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.feature.impl.PathExecutor;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.pathfinder.calculate.Path;
import com.jelly.mightyminerv2.pathfinder.calculate.path.AStarPathFinder;
import com.jelly.mightyminerv2.pathfinder.goal.Goal;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import java.awt.Color;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import kotlin.Pair;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Pathfinder
extends AbstractFeature {
    private static Pathfinder instance;
    private final Deque<Pair<BlockPos, BlockPos>> pathQueue = new ConcurrentLinkedDeque<Pair<BlockPos, BlockPos>>();
    private final PathExecutor pathExecutor = PathExecutor.getInstance();
    private AStarPathFinder finder;
    private volatile boolean skipTick = false;
    private volatile boolean pathfinding = false;
    private boolean failed = false;
    private boolean succeeded = false;

    public static Pathfinder getInstance() {
        if (instance == null) {
            instance = new Pathfinder();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "Pathfinder";
    }

    @Override
    public void start() {
        if (this.pathQueue.isEmpty()) {
            this.error("Pathqueue is empty. Cannot start");
            return;
        }
        this.enabled = true;
        this.succeeded = false;
        this.failed = false;
        this.pathExecutor.start();
        this.send("Started");
    }

    @Override
    public void stop() {
        this.enabled = false;
        this.pathfinding = false;
        this.skipTick = false;
        this.pathQueue.clear();
        this.resetStatesAfterStop();
        this.send("stopped");
    }

    @Override
    public void resetStatesAfterStop() {
        if (this.finder != null) {
            this.finder.requestStop();
        }
        this.pathExecutor.stop();
        RotationHandler.getInstance().stop();
    }

    public void queue(BlockPos start2, BlockPos end) {
        if (!this.pathQueue.isEmpty() && !((BlockPos)this.pathQueue.peekLast().getSecond()).equals((Object)start2)) {
            this.error("This does not start at the end of the previous path. Ignoring!");
            return;
        }
        this.pathQueue.offer((Pair<BlockPos, BlockPos>)new Pair((Object)start2, (Object)end));
        this.log("Queued Path");
    }

    public void stopAndRequeue(BlockPos pos) {
        if (this.enabled) {
            this.pathQueue.clear();
            this.pathExecutor.clearQueue();
            this.finder.requestStop();
        }
        this.queue(pos);
    }

    public void queue(BlockPos end) {
        BlockPos start2 = this.pathQueue.isEmpty() ? (this.pathExecutor.getCurrentPath() == null ? PlayerUtil.getBlockStandingOn() : this.pathExecutor.getCurrentPath().getEnd()) : (BlockPos)this.pathQueue.peekLast().getFirst();
        this.pathQueue.offer((Pair<BlockPos, BlockPos>)new Pair((Object)start2, (Object)end));
    }

    public void setSprintState(boolean sprint) {
        this.pathExecutor.setAllowSprint(sprint);
    }

    public void setInterpolationState(boolean interpolate) {
        this.pathExecutor.setAllowInterpolation(interpolate);
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        boolean okToPath = false;
        if (event.phase == TickEvent.Phase.START) {
            okToPath = this.pathExecutor.onTick();
        }
        if (this.skipTick) {
            this.skipTick = false;
            return;
        }
        if (this.pathExecutor.failed()) {
            this.log("pathexeutor failed");
            this.failed = true;
            this.stop();
            return;
        }
        if (!okToPath) {
            return;
        }
        if (this.pathQueue.isEmpty()) {
            if (this.pathExecutor.getState() == PathExecutor.State.WAITING && !this.pathfinding) {
                this.stop();
                this.succeeded = true;
                this.log("pathqueue empty stopping");
            }
            return;
        }
        if (this.pathfinding) {
            return;
        }
        MightyMiner.executor().execute(() -> {
            this.log("creating thread. wasPathfinding: " + this.pathfinding);
            if (this.pathfinding) {
                return;
            }
            this.pathfinding = true;
            try {
                Pair<BlockPos, BlockPos> startEnd = this.pathQueue.poll();
                if (startEnd == null) {
                    this.pathfinding = false;
                    return;
                }
                BlockPos start2 = (BlockPos)startEnd.getFirst();
                BlockPos end = (BlockPos)startEnd.getSecond();
                double walkSpeed = this.mc.field_71439_g.func_70689_ay();
                CalculationContext ctx = new CalculationContext(walkSpeed * 1.3, walkSpeed, walkSpeed * 0.3);
                this.finder = new AStarPathFinder(start2.func_177958_n(), start2.func_177956_o(), start2.func_177952_p(), new Goal(end.func_177958_n(), end.func_177956_o(), end.func_177952_p(), ctx), ctx);
                Path path = this.finder.calculatePath();
                this.log("done pathfinding");
                if (path != null) {
                    path.getSmoothedPath();
                    PathExecutor.getInstance().queuePath(path);
                    this.log("starting pathexec");
                } else {
                    this.log("No Path Found");
                    this.failed = true;
                    this.stop();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.pathfinding = false;
            this.skipTick = true;
        });
    }

    @Override
    @SubscribeEvent
    protected void onRender(RenderWorldLastEvent event) {
        if (!this.enabled) {
            return;
        }
        LinkedList<Path> paths = new LinkedList<Path>(this.pathExecutor.getPathQueue());
        if (this.pathExecutor.getCurrentPath() != null) {
            paths.add(this.pathExecutor.getCurrentPath());
        }
        if (!paths.isEmpty()) {
            paths.forEach(path -> {
                List<BlockPos> bpath = path.getSmoothedPath();
                for (int i = 0; i < bpath.size(); ++i) {
                    RenderUtil.drawBlock(bpath.get(i), new Color(0, 255, 0, 150));
                    if (i == 0) continue;
                    RenderUtil.drawLine(new Vec3((Vec3i)bpath.get(i)).func_72441_c(0.5, 1.0, 0.5), new Vec3((Vec3i)bpath.get(i - 1)).func_72441_c(0.5, 1.0, 0.5), new Color(0, 255, 0, 150));
                }
            });
        }
    }

    public boolean completedPathTo(BlockPos pos) {
        Path prev = this.pathExecutor.getPreviousPath();
        return prev != null && prev.getGoal().isAtGoal(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    public boolean failed() {
        return !this.enabled && this.failed;
    }

    public boolean succeeded() {
        return !this.enabled && this.succeeded;
    }
}

