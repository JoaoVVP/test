/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.BlockChangeEvent;
import com.jelly.mightyminerv2.event.BlockDestroyEvent;
import com.jelly.mightyminerv2.event.SpawnParticleEvent;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.BlockUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RaytracingUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.StrafeUtil;
import com.jelly.mightyminerv2.util.helper.Clock;
import com.jelly.mightyminerv2.util.helper.MineableBlock;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BlockMiner
extends AbstractFeature {
    private static BlockMiner instance;
    private static BoostState boostState;
    private final Random random = new Random();
    private Map<Integer, Integer> blocks = new HashMap<Integer, Integer>();
    private int[] priority = new int[]{1, 1, 1, 1};
    private State state = State.STARTING;
    private BlockMinerError mithrilError = BlockMinerError.NONE;
    private int miningSpeed = 200;
    private int miningTime = 0;
    private int speedBoost = 0;
    private BlockPos targetBlock = null;
    private Vec3 targetPoint = null;
    private Vec3 destBlock = null;
    private int breakAttemptTime = 0;
    private final Clock shiftTimer = new Clock();
    public int wait_threshold = 2000;

    public static BlockMiner getInstance() {
        if (instance == null) {
            instance = new BlockMiner();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "BlockMiner";
    }

    @Override
    public void stop() {
        if (!this.enabled) {
            return;
        }
        this.enabled = false;
        this.miningSpeed = 200;
        this.miningTime = 0;
        this.speedBoost = 0;
        this.destBlock = null;
        this.targetPoint = null;
        KeyBindUtil.releaseAllExcept(new KeyBinding[0]);
        StrafeUtil.enabled = false;
        this.resetStatesAfterStop();
        this.send("Disabled");
    }

    @Override
    public void resetStatesAfterStop() {
        this.timer.reset();
        this.state = State.STARTING;
        RotationHandler.getInstance().stop();
    }

    public void start(MineableBlock[] blockToMine, int[] priority) {
        this.start(blockToMine, 200, 100, priority, "");
    }

    public void start(MineableBlock[] blocksToMine, int miningSpeed, int speedBoost, int[] priority, String miningTool) {
        if (!miningTool.isEmpty() && !InventoryUtil.holdItem(miningTool)) {
            this.error("Cannot hold " + miningTool);
            this.stop(BlockMinerError.NOT_ENOUGH_BLOCKS);
            return;
        }
        if (blocksToMine == null) {
            this.error("No blocks to mine.");
            this.stop(BlockMinerError.NOT_ENOUGH_BLOCKS);
            return;
        }
        for (int i = 0; i < blocksToMine.length; ++i) {
            for (int j : blocksToMine[i].stateIds) {
                this.blocks.put(j, i);
            }
        }
        this.miningSpeed = miningSpeed;
        this.speedBoost = speedBoost;
        this.priority = priority;
        this.enabled = true;
        this.mithrilError = BlockMinerError.NONE;
        this.start();
        this.send("Enabled");
    }

    public void stop(BlockMinerError error) {
        this.mithrilError = error;
        this.stop();
    }

    public boolean hasSucceeded() {
        return !this.enabled && this.mithrilError == BlockMinerError.NONE;
    }

    public BlockMinerError getMithrilError() {
        return this.mithrilError;
    }

    public static BoostState getBoostState() {
        return boostState;
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled || this.mc.field_71462_r != null || event.phase == TickEvent.Phase.END) {
            return;
        }
        if (this.shiftTimer.isScheduled() && this.shiftTimer.passed()) {
            KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74311_E, MightyMinerConfig.mithrilMinerSneakWhileMining);
            this.shiftTimer.reset();
        }
        switch (this.state) {
            case STARTING: {
                this.breakAttemptTime = 0;
                this.swapState(State.CHOOSING_BLOCK, 0);
                if (boostState.ordinal() <= 1) {
                    this.swapState(State.SPEED_BOOST, 250);
                    KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74312_F, false);
                }
                this.log("Starting. stopped walking");
                break;
            }
            case SPEED_BOOST: {
                if (this.isTimerRunning()) {
                    return;
                }
                KeyBindUtil.rightClick();
                this.swapState(State.STARTING, 0);
                boostState = BoostState.INACTIVE;
                break;
            }
            case CHOOSING_BLOCK: {
                List<BlockPos> blocks;
                if (!MightyMinerConfig.mithrilStrafe || (blocks = BlockUtil.getBestMineableBlocksAround(this.blocks, this.priority, this.targetBlock, this.miningSpeed)).isEmpty()) {
                    blocks = BlockUtil.getBestMineableBlocks(this.blocks, this.priority, this.targetBlock, this.miningSpeed);
                }
                if (blocks.size() < 2) {
                    if (!this.timer.isScheduled()) {
                        this.log("Scheduled a 2 second timer to see if mithril spawns back or not");
                        this.timer.schedule(this.wait_threshold);
                    }
                    if (this.hasTimerEnded()) {
                        this.error("Cannot find enough mithril blocks to mine.");
                        this.stop(BlockMinerError.NOT_ENOUGH_BLOCKS);
                    }
                    return;
                }
                if (this.timer.isScheduled()) {
                    this.timer.reset();
                }
                this.targetBlock = blocks.get(blocks.get(0).equals((Object)this.targetBlock) ? 1 : 0);
                int boostMultiplier = boostState == BoostState.ACTIVE ? this.speedBoost / 100 : 1;
                this.miningTime = BlockUtil.getMiningTime(Block.func_176210_f((IBlockState)this.mc.field_71441_e.func_180495_p(this.targetBlock)), this.miningSpeed * boostMultiplier);
                this.swapState(State.ROTATING, 0);
            }
            case ROTATING: {
                List<Vec3> points = BlockUtil.bestPointsOnBestSide(this.targetBlock);
                if (points.isEmpty()) {
                    this.swapState(State.STARTING, 0);
                    this.log("Cannot find points to look at");
                    break;
                }
                this.targetPoint = points.get(0);
                RotationHandler.getInstance().stop();
                RotationHandler.getInstance().queueRotation(new RotationConfiguration(new Target(this.targetPoint), (long)this.getRandomRotationTime(), null));
                if (this.random.nextBoolean()) {
                    int halfwayMark = points.size() / 2;
                    this.targetPoint = points.get(this.random.nextInt(halfwayMark) + halfwayMark - 1);
                    RotationHandler.getInstance().queueRotation(new RotationConfiguration(new Target(this.targetPoint), (long)this.getRandomRotationTime() * 2L, null));
                }
                RotationHandler.getInstance().start();
                if (this.targetPoint != null && PlayerUtil.getPlayerEyePos().func_72438_d(this.targetPoint) > 4.0) {
                    this.swapState(State.WALKING, 0);
                    Vec3 vec = AngleUtil.getVectorForRotation(AngleUtil.getRotationYaw(this.targetPoint));
                    if (this.mc.field_71441_e.func_175623_d(new BlockPos(this.mc.field_71439_g.func_174791_d().func_178787_e(vec)))) {
                        this.destBlock = BlockUtil.getWalkableBlocksAround(PlayerUtil.getBlockStandingOn()).stream().min(Comparator.comparingDouble(arg_0 -> ((BlockPos)this.targetBlock).func_177951_i(arg_0))).map(b -> new Vec3((double)b.func_177958_n() + 0.5, (double)b.func_177956_o(), (double)b.func_177952_p() + 0.5)).orElse(null);
                    }
                } else {
                    this.swapState(State.BREAKING, 0);
                }
                this.log("Rotating");
            }
            case WALKING: {
                if ((this.destBlock == null || Math.hypot(this.destBlock.field_72450_a - this.mc.field_71439_g.field_70165_t, this.destBlock.field_72449_c - this.mc.field_71439_g.field_70161_v) > 0.2) && PlayerUtil.getPlayerEyePos().func_72438_d(this.targetPoint) > 3.5) {
                    this.shiftTimer.reset();
                    StrafeUtil.enabled = true;
                    StrafeUtil.yaw = AngleUtil.getRotationYaw360(this.destBlock == null ? this.targetPoint : this.destBlock);
                    KeyBindUtil.holdThese(this.mc.field_71474_y.field_74351_w, this.mc.field_71474_y.field_74311_E);
                } else {
                    this.destBlock = null;
                    StrafeUtil.enabled = false;
                    this.swapState(State.BREAKING, 0);
                    this.shiftTimer.schedule(this.getRandomSneakTime());
                    KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74351_w, false);
                }
            }
            case BREAKING: {
                KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74312_F, true);
                if (++this.breakAttemptTime > this.miningTime + 60) {
                    this.log("BreakAttempTime > MiningTime + 60");
                    this.swapState(State.STARTING, 0);
                }
                if (this.targetBlock.equals((Object)BlockUtil.getBlockLookingAt())) {
                    if (--this.miningTime <= 0) {
                        this.log("MiningTime <= 0");
                        this.swapState(State.STARTING, 0);
                    }
                } else if (this.state != State.WALKING && !RotationHandler.getInstance().isEnabled()) {
                    this.log("!Rotating");
                    this.swapState(State.STARTING, 0);
                }
                if (!StrafeUtil.enabled || this.state != State.BREAKING) break;
                this.destBlock = null;
                StrafeUtil.enabled = false;
                this.shiftTimer.schedule(this.getRandomSneakTime());
                KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74351_w, false);
            }
        }
    }

    @Override
    @SubscribeEvent
    protected void onBlockChange(BlockChangeEvent event) {
        if (!this.enabled || this.state.ordinal() < State.ROTATING.ordinal()) {
            return;
        }
        if (event.pos.equals((Object)this.targetBlock)) {
            RotationHandler.getInstance().stop();
            this.swapState(State.STARTING, 0);
        }
    }

    @Override
    @SubscribeEvent
    public void onBlockDestroy(BlockDestroyEvent event) {
        if (!this.enabled || !event.getBlock().equals((Object)this.targetBlock)) {
            return;
        }
        this.breakAttemptTime = 0;
    }

    @SubscribeEvent
    public void onParticleSpawn(SpawnParticleEvent event) {
        if (!this.enabled || !MightyMinerConfig.mithrilMinerPrecisionMiner) {
            return;
        }
        int particleID = event.getParticleTypes().func_179348_c();
        if (particleID == 9 || particleID == 10) {
            Vec3 particlePos = new Vec3(event.getXCoord(), event.getYCoord(), event.getZCoord());
            MovingObjectPosition raytrace = RaytracingUtil.raytraceTowards(PlayerUtil.getPlayerEyePos(), particlePos, 4.0);
            if (raytrace != null && this.targetBlock.equals((Object)raytrace.func_178782_a())) {
                this.log("stopped rot");
                RotationHandler.getInstance().stop();
                RotationHandler.getInstance().easeTo(new RotationConfiguration(new Target(particlePos), (long)this.getRandomRotationTime(), null));
            }
        }
    }

    @SubscribeEvent
    protected void onChat(ClientChatReceivedEvent event) {
        if (event.type != 0) {
            return;
        }
        String message = event.message.func_150260_c();
        if (message.equals("Mining Speed Boost is now available!")) {
            boostState = BoostState.AVAILABLE;
        }
        if (message.contains("You used your Mining Speed Boost Pickaxe Ability!")) {
            boostState = BoostState.ACTIVE;
        }
        if (message.equals("Your Mining Speed Boost has expired!") || boostState != BoostState.ACTIVE && message.startsWith("This ability is on cooldown for")) {
            boostState = BoostState.INACTIVE;
        }
    }

    @Override
    @SubscribeEvent
    protected void onRender(RenderWorldLastEvent event) {
        if (!this.enabled) {
            return;
        }
        if (this.targetBlock != null) {
            RenderUtil.drawBlock(this.targetBlock, new Color(0, 255, 255, 100));
        }
        if (this.targetPoint != null) {
            RenderUtil.drawPoint(this.targetPoint, new Color(255, 0, 0, 150));
        }
    }

    private void swapState(State toState, int delay) {
        this.state = toState;
        if (delay == 0) {
            this.timer.reset();
        } else {
            this.timer.schedule(delay);
        }
    }

    private int getRandomRotationTime() {
        return MightyMinerConfig.mithrilMinerRotationTime + (int)(this.random.nextFloat() * (float)MightyMinerConfig.mithrilMinerRotationTimeRandomizer);
    }

    private int getRandomSneakTime() {
        return MightyMinerConfig.mithrilMinerSneakTime + (int)(this.random.nextFloat() * (float)MightyMinerConfig.mithrilMinerSneakTimeRandomizer);
    }

    static {
        boostState = BoostState.UNKNOWN;
    }

    public static enum BlockMinerError {
        NONE,
        NOT_ENOUGH_BLOCKS;

    }

    static enum BoostState {
        UNKNOWN,
        AVAILABLE,
        ACTIVE,
        INACTIVE;

    }

    static enum State {
        STARTING,
        SPEED_BOOST,
        CHOOSING_BLOCK,
        ROTATING,
        WALKING,
        BREAKING;

    }
}

