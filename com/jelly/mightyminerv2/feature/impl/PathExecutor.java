/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.pathfinder.calculate.Path;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.BlockUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.StrafeUtil;
import com.jelly.mightyminerv2.util.helper.Angle;
import com.jelly.mightyminerv2.util.helper.Clock;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class PathExecutor {
    private static PathExecutor instance;
    private boolean enabled = false;
    private final Minecraft mc = Minecraft.func_71410_x();
    private final Deque<Path> pathQueue = new LinkedList<Path>();
    private Path prev;
    private Path curr;
    private final Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
    private final List<BlockPos> blockPath = new ArrayList<BlockPos>();
    private boolean failed = false;
    private boolean succeeded = false;
    private boolean pastTarget = false;
    private int target = 0;
    private int previous = -1;
    private long nodeChangeTime = 0L;
    private boolean interpolated = true;
    private float interpolYawDiff = 0.0f;
    private State state = State.STARTING_PATH;
    private boolean allowSprint = true;
    private boolean allowInterpolation = false;
    private final Clock stuckTimer = new Clock();

    public static PathExecutor getInstance() {
        if (instance == null) {
            instance = new PathExecutor();
        }
        return instance;
    }

    public void queuePath(Path path) {
        Path lastPath;
        if (path.getPath().isEmpty()) {
            this.error("Path is empty");
            this.failed = true;
            return;
        }
        BlockPos start2 = path.getStart();
        Path path2 = lastPath = this.curr != null ? this.curr : this.pathQueue.peekLast();
        if (lastPath != null && !lastPath.getGoal().isAtGoal(start2.func_177958_n(), start2.func_177956_o(), start2.func_177952_p())) {
            this.error("This path segment does not start at last path's goal. LastpathGoal: " + lastPath.getGoal() + ", ThisPathStart: " + start2);
            this.failed = true;
            return;
        }
        this.pathQueue.offer(path);
    }

    public void start() {
        this.state = State.STARTING_PATH;
        this.enabled = true;
    }

    public void stop() {
        this.enabled = false;
        this.pathQueue.clear();
        this.blockPath.clear();
        this.map.clear();
        this.curr = null;
        this.prev = null;
        this.target = 0;
        this.previous = -1;
        this.pastTarget = false;
        this.state = State.END;
        this.interpolYawDiff = 0.0f;
        this.allowSprint = true;
        this.allowInterpolation = false;
        this.nodeChangeTime = 0L;
        this.interpolated = true;
        StrafeUtil.enabled = false;
        RotationHandler.getInstance().stop();
        KeyBindUtil.releaseAllExcept(new KeyBinding[0]);
    }

    public void clearQueue() {
        this.pathQueue.clear();
        this.curr = null;
        this.succeeded = true;
        this.failed = false;
        this.interpolated = false;
        this.target = 0;
        this.previous = -1;
    }

    public void setAllowSprint(boolean sprint) {
        this.allowSprint = sprint;
    }

    public void setAllowInterpolation(boolean interpolate) {
        this.allowInterpolation = interpolate;
    }

    public boolean onTick() {
        if (!this.enabled) {
            return false;
        }
        if (this.stuckTimer.isScheduled() && this.stuckTimer.passed()) {
            this.log("Was Stuck For a Second.");
            this.failed = true;
            this.succeeded = false;
            this.stop();
        }
        BlockPos playerPos = PlayerUtil.getBlockStandingOn();
        if (this.curr != null) {
            List<Long> blockHashes = this.map.get(this.pack(playerPos.func_177958_n(), playerPos.func_177952_p()));
            int current = -1;
            if (blockHashes != null && !blockHashes.isEmpty()) {
                int bestY = -1;
                double playerY = this.mc.field_71439_g.field_70163_u;
                for (Long blockHash : blockHashes) {
                    Pair<Integer, Integer> block = this.unpack(blockHash);
                    int blockY = (Integer)block.getFirst();
                    int blockTarget = (Integer)block.getSecond();
                    if (blockTarget <= this.previous || !(bestY == -1 || (double)blockY < playerY && blockY > bestY) && (!((double)blockY >= playerY) || blockY >= bestY)) continue;
                    bestY = (Integer)block.getFirst();
                    current = blockTarget;
                }
            }
            if (current != -1 && current > this.previous) {
                this.previous = current;
                this.target = current + 1;
                this.state = State.TRAVERSING;
                this.pastTarget = false;
                this.interpolated = false;
                this.interpolYawDiff = 0.0f;
                this.nodeChangeTime = System.currentTimeMillis();
                this.log("changed target from " + this.previous + " to " + this.target);
                RotationHandler.getInstance().stop();
            }
            if (Math.hypot(this.mc.field_71439_g.field_70159_w, this.mc.field_71439_g.field_70179_y) < 0.05) {
                if (!this.stuckTimer.isScheduled()) {
                    this.stuckTimer.schedule(1000L);
                }
            } else {
                this.stuckTimer.reset();
            }
        } else {
            if (this.stuckTimer.isScheduled()) {
                this.stuckTimer.reset();
            }
            if (this.pathQueue.isEmpty()) {
                return true;
            }
        }
        if (this.curr == null || this.target == this.blockPath.size()) {
            this.log("Path traversed");
            if (this.pathQueue.isEmpty()) {
                this.log("Pathqueue is empty");
                if (this.curr != null) {
                    this.curr = null;
                    this.target = 0;
                    this.previous = -1;
                }
                this.state = State.WAITING;
                return true;
            }
            this.succeeded = true;
            this.failed = false;
            this.prev = this.curr;
            this.target = 1;
            this.previous = 0;
            this.loadPath(this.pathQueue.poll());
            if (this.target == this.blockPath.size()) {
                return true;
            }
            this.log("loaded new path target: " + this.target + ", prev: " + this.previous);
        }
        BlockPos target = this.blockPath.get(this.target);
        if (this.target < this.blockPath.size() - 1) {
            BlockPos nextTarget = this.blockPath.get(this.target + 1);
            double playerDistToNext = playerPos.func_177951_i((Vec3i)nextTarget);
            double targetDistToNext = target.func_177951_i((Vec3i)nextTarget);
            if ((this.pastTarget || (this.pastTarget = playerDistToNext > targetDistToNext)) && playerDistToNext < targetDistToNext) {
                this.previous = this.target++;
                target = this.blockPath.get(this.target);
                this.log("walked past target");
            }
        }
        boolean onGround = this.mc.field_71439_g.field_70122_E;
        int targetX = target.func_177958_n();
        int targetZ = target.func_177952_p();
        double horizontalDistToTarget = Math.hypot(this.mc.field_71439_g.field_70165_t - (double)targetX - 0.5, this.mc.field_71439_g.field_70161_v - (double)targetZ - 0.5);
        float yaw = AngleUtil.getRotationYaw360(this.mc.field_71439_g.func_174791_d(), new Vec3((double)targetX + 0.5, 0.0, (double)targetZ + 0.5));
        float yawDiff = Math.abs(AngleUtil.get360RotationYaw() - yaw);
        if (this.interpolYawDiff == 0.0f) {
            this.interpolYawDiff = yaw - AngleUtil.get360RotationYaw();
        }
        if (yawDiff > 3.0f && !RotationHandler.getInstance().isEnabled()) {
            float rotYaw = yaw;
            for (int i = this.target; i < this.blockPath.size(); ++i) {
                BlockPos rotationTarget = this.blockPath.get(i);
                if (!(Math.hypot(this.mc.field_71439_g.field_70165_t - (double)rotationTarget.func_177958_n(), this.mc.field_71439_g.field_70161_v - (double)rotationTarget.func_177952_p()) > 5.0)) continue;
                rotYaw = AngleUtil.getRotation((BlockPos)rotationTarget).yaw;
                break;
            }
            float time = MightyMinerConfig.fixrot ? (float)MightyMinerConfig.rottime : (float)Math.max(300L, (long)(400.0 - horizontalDistToTarget * (double)MightyMinerConfig.rotmult));
            RotationHandler.getInstance().easeTo(new RotationConfiguration(new Angle(rotYaw, 15.0f), (long)time, null));
        }
        float ipYaw = yaw;
        if (onGround && horizontalDistToTarget >= 8.0 && this.allowInterpolation && !this.interpolated) {
            float time = 200.0f;
            long timePassed = Math.min((long)time, System.currentTimeMillis() - this.nodeChangeTime);
            ipYaw -= this.interpolYawDiff * (1.0f - (float)timePassed / time);
            if ((float)timePassed == time) {
                this.interpolated = true;
            }
        }
        StrafeUtil.enabled = yawDiff > 3.0f;
        StrafeUtil.yaw = ipYaw;
        Vec3 pos = new Vec3(this.mc.field_71439_g.field_70165_t, (double)playerPos.func_177956_o() + 0.5, this.mc.field_71439_g.field_70161_v);
        Vec3 vec4Rot = AngleUtil.getVectorForRotation(yaw);
        boolean shouldJump = BlockUtil.canWalkBetween(this.curr.getCtx(), pos, pos.func_72441_c(vec4Rot.field_72450_a, 1.0, vec4Rot.field_72449_c));
        KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74351_w, true);
        KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_151444_V, this.allowSprint && yawDiff < 40.0f && !shouldJump);
        if (shouldJump && onGround) {
            this.mc.field_71439_g.func_70664_aZ();
            this.state = State.JUMPING;
        }
        KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74314_A, shouldJump);
        return playerPos.func_177957_d((double)target.func_177958_n(), (double)target.func_177956_o(), (double)target.func_177952_p()) < 100.0;
    }

    public void loadPath(Path path) {
        this.blockPath.clear();
        this.map.clear();
        this.curr = path;
        this.blockPath.addAll(this.curr.getSmoothedPath());
        for (int i = 0; i < this.blockPath.size(); ++i) {
            BlockPos pos = this.blockPath.get(i);
            this.map.computeIfAbsent(this.pack(pos.func_177958_n(), pos.func_177952_p()), k -> new ArrayList()).add(this.pack(pos.func_177956_o(), i));
        }
    }

    public void onRender() {
        System.out.println("OnRender");
        if (this.target != -1 && this.target < this.blockPath.size()) {
            System.out.println("valtarg");
            BlockPos playerPos = PlayerUtil.getBlockStandingOn();
            BlockPos target = this.blockPath.get(this.target);
            int targetX = target.func_177958_n();
            int targetZ = target.func_177952_p();
            float yaw = AngleUtil.getRotationYaw360(this.mc.field_71439_g.func_174791_d(), new Vec3((double)targetX + 0.5, 0.0, (double)targetZ + 0.5));
            Vec3 pos = new Vec3(this.mc.field_71439_g.field_70165_t, (double)playerPos.func_177956_o() + 0.5, this.mc.field_71439_g.field_70161_v);
            Vec3 vec4Rot = AngleUtil.getVectorForRotation(yaw);
            Vec3 newV = pos.func_72441_c(vec4Rot.field_72450_a, (double)(playerPos.func_177956_o() + 1), vec4Rot.field_72449_c);
            RenderUtil.drawBlock(new BlockPos(MathHelper.func_76128_c((double)newV.field_72450_a), MathHelper.func_76128_c((double)newV.field_72448_b), MathHelper.func_76128_c((double)newV.field_72449_c)), new Color(255, 0, 0, 255));
            RenderUtil.drawBlock(playerPos, new Color(255, 255, 0, 100));
        }
    }

    public State getState() {
        return this.state;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Path getPreviousPath() {
        return this.prev;
    }

    public Path getCurrentPath() {
        return this.curr;
    }

    public Deque<Path> getPathQueue() {
        return this.pathQueue;
    }

    public boolean failed() {
        return !this.enabled && this.failed;
    }

    public boolean ended() {
        return !this.enabled && this.succeeded;
    }

    private long pack(int x, int z) {
        return (long)x << 32 | (long)z & 0xFFFFFFFFL;
    }

    public Pair<Integer, Integer> unpack(long packed) {
        return new Pair((Object)((int)(packed >> 32)), (Object)((int)packed));
    }

    void log(String message) {
        Logger.sendLog(this.getMessage(message));
    }

    void send(String message) {
        Logger.sendMessage(this.getMessage(message));
    }

    void error(String message) {
        Logger.sendError(this.getMessage(message));
    }

    void note(String message) {
        Logger.sendNote(this.getMessage(message));
    }

    String getMessage(String message) {
        return "[PathExecutor] " + message;
    }

    static enum State {
        STARTING_PATH,
        TRAVERSING,
        JUMPING,
        WAITING,
        END;

    }
}

