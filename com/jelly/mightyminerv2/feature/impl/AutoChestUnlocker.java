/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.event.BlockChangeEvent;
import com.jelly.mightyminerv2.event.SpawnParticleEvent;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.feature.impl.Pathfinder;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.util.BlockUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockChest;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoChestUnlocker
extends AbstractFeature {
    public static AutoChestUnlocker instance = new AutoChestUnlocker();
    public static List<BlockPos> chestQueue = new ArrayList<BlockPos>();
    private State state = State.STARTING;
    private Vec3 particlePos = null;
    private boolean particleSpawned = true;
    private boolean chestSolved = false;
    private BlockPos chestSolving = null;
    private List<BlockPos> walkableBlocks = new ArrayList<BlockPos>();
    private boolean clickChest = false;
    public ChestFailure chestFailure = ChestFailure.NONE;

    @Override
    public String getName() {
        return "TreasureChestUnlocker";
    }

    public void start(boolean clickChest) {
        this.start("", clickChest);
    }

    public void start(String itemToHold, boolean clickChest) {
        if (!itemToHold.isEmpty()) {
            InventoryUtil.holdItem(itemToHold);
        }
        this.chestFailure = ChestFailure.NONE;
        this.clickChest = clickChest;
        this.enabled = true;
        this.note("Started");
    }

    @Override
    public void stop() {
        this.enabled = false;
        this.particlePos = null;
        this.chestSolving = null;
        this.walkableBlocks.clear();
        this.clickChest = false;
        this.resetStatesAfterStop();
        this.note("Stopped");
    }

    @Override
    public void resetStatesAfterStop() {
        this.state = State.STARTING;
    }

    private void stop(ChestFailure failure) {
        this.chestFailure = failure;
        this.stop();
    }

    private void changeState(State to, int time) {
        this.state = to;
        if (time == 0) {
            this.timer.reset();
        } else {
            this.timer.schedule(time);
        }
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        switch (this.state) {
            case STARTING: {
                if (chestQueue.isEmpty()) {
                    this.log("Chestqueue is empty");
                    this.changeState(State.ENDING, 0);
                    break;
                }
                chestQueue.sort(Comparator.comparing(arg_0 -> ((EntityPlayerSP)this.mc.field_71439_g).func_174831_c(arg_0)));
                this.chestSolving = chestQueue.remove(0);
                Vec3 vec3 = new Vec3((Vec3i)this.chestSolving);
                if (PlayerUtil.getPlayerEyePos().func_72438_d(vec3.func_72441_c(0.5, 0.5, 0.5)) > 4.0) {
                    this.changeState(State.FINDING_WALKABLE_BLOCKS, 0);
                    break;
                }
                this.changeState(State.LOOKING, 0);
                break;
            }
            case FINDING_WALKABLE_BLOCKS: {
                ArrayList<BlockPos> pos = new ArrayList<BlockPos>();
                for (int y = -4; y < 0; ++y) {
                    for (int x = -1; x < 2; ++x) {
                        for (int z = -1; z < 2; ++z) {
                            BlockPos newPos = this.chestSolving.func_177982_a(x, y, z);
                            if (!BlockUtil.canStandOn(newPos)) continue;
                            pos.add(newPos);
                        }
                    }
                }
                pos.sort(Comparator.comparing(arg_0 -> ((EntityPlayerSP)this.mc.field_71439_g).func_174831_c(arg_0)));
                this.walkableBlocks = pos;
                this.changeState(State.WALKING, 0);
                this.log("Found block list size: " + pos.size());
                break;
            }
            case WALKING: {
                if (this.walkableBlocks.isEmpty()) {
                    this.error("no walkable blocks around this chest. ignoring");
                    this.changeState(State.STARTING, 0);
                    break;
                }
                BlockPos target = this.walkableBlocks.remove(0);
                this.log("Walking to " + target);
                Pathfinder.getInstance().setInterpolationState(true);
                Pathfinder.getInstance().queue(PlayerUtil.getBlockStandingOn(), target);
                Pathfinder.getInstance().start();
                this.changeState(State.WAITING, 0);
                break;
            }
            case WAITING: {
                if (Pathfinder.getInstance().isRunning()) {
                    Vec3 vec3 = new Vec3((Vec3i)this.chestSolving);
                    if (!(PlayerUtil.getPlayerEyePos().func_72438_d(vec3.func_72441_c(0.5, 0.5, 0.5)) < 3.0)) break;
                    this.log("distance < 4");
                    Pathfinder.getInstance().stop();
                    this.changeState(State.LOOKING, 5000);
                    break;
                }
                if (Pathfinder.getInstance().succeeded()) {
                    this.log("Pathfinder succeeded");
                    this.changeState(State.LOOKING, 5000);
                    break;
                }
                this.error("failed walking to block. retrying");
                this.changeState(State.WALKING, 0);
                break;
            }
            case LOOKING: {
                if (!this.chestSolving.equals((Object)BlockUtil.getBlockLookingAt()) && !RotationHandler.getInstance().isEnabled()) {
                    RotationHandler.getInstance().easeTo(new RotationConfiguration(new Target(BlockUtil.getClosestVisibleSidePos(this.chestSolving)), 400L, null));
                }
                if (this.clickChest) {
                    this.changeState(State.VERIFYING_ROTATION, 3000);
                    break;
                }
                if (this.hasTimerEnded()) {
                    this.error("no particle spawned in over 2 seconds.");
                    this.changeState(State.STARTING, 0);
                    break;
                }
                if (this.chestSolved) {
                    this.log("chest solved. stopping rotation and going to next");
                    RotationHandler.getInstance().stop();
                    this.changeState(State.STARTING, 0);
                    this.chestSolved = false;
                    break;
                }
                if (!this.particleSpawned || this.particlePos == null) break;
                RotationHandler.getInstance().stopFollowingTarget();
                RotationHandler.getInstance().easeTo(new RotationConfiguration(new Target(this.particlePos), 400L, null).followTarget(true));
                this.timer.schedule(5000L);
                this.particleSpawned = false;
                break;
            }
            case VERIFYING_ROTATION: {
                if (this.hasTimerEnded()) {
                    this.error("Couldn't look at chest.");
                    this.changeState(State.STARTING, 0);
                }
                if (this.chestSolving.equals((Object)BlockUtil.getBlockLookingAt())) {
                    RotationHandler.getInstance().stop();
                    KeyBindUtil.releaseAllExcept(new KeyBinding[0]);
                    this.changeState(State.CLICKING, 250);
                    break;
                }
                if (RotationHandler.getInstance().isEnabled()) break;
                KeyBindUtil.holdThese(this.mc.field_71474_y.field_74312_F);
                break;
            }
            case CLICKING: {
                if (this.isTimerRunning()) {
                    return;
                }
                KeyBindUtil.rightClick();
                this.changeState(State.STARTING, 0);
                break;
            }
            case ENDING: {
                this.log("Ended. Stopping");
                this.stop();
            }
        }
    }

    @SubscribeEvent
    protected void onParticleSpawn(SpawnParticleEvent event) {
        IBlockState state;
        if (!this.enabled || this.state != State.LOOKING || !this.clickChest) {
            return;
        }
        if (event.getParticleTypes() == EnumParticleTypes.CRIT && this.mc.field_71439_g.func_174791_d().func_72436_e(event.getPos()) < 64.0 && (state = this.mc.field_71441_e.func_180495_p(this.chestSolving)).func_177230_c() instanceof BlockChest && this.chestSolving.func_177972_a((EnumFacing)state.func_177229_b((IProperty)BlockChest.field_176459_a)).equals((Object)new BlockPos(event.getPos()))) {
            this.particlePos = event.getPos();
            this.particleSpawned = true;
        }
    }

    @Override
    @SubscribeEvent
    protected void onRender(RenderWorldLastEvent event) {
        chestQueue.forEach(it -> RenderUtil.drawBlock(it, new Color(0, 255, 255, 100)));
        if (!this.enabled) {
            return;
        }
        if (this.chestSolving != null) {
            RenderUtil.drawBlock(this.chestSolving, new Color(0, 255, 0, 100));
        }
        if (this.particlePos != null) {
            RenderUtil.drawPoint(this.particlePos, new Color(255, 0, 0, 100));
        }
    }

    @Override
    @SubscribeEvent
    protected void onBlockChange(BlockChangeEvent event) {
        if (this.mc.field_71439_g == null) {
            return;
        }
        BlockPos eventPos = event.pos;
        if (this.mc.field_71439_g.func_174818_b(eventPos) > 64.0) {
            return;
        }
        Block newBlock = event.update.func_177230_c();
        Block oldBlock = event.old.func_177230_c();
        if (oldBlock instanceof BlockAir && newBlock instanceof BlockChest) {
            chestQueue.add(eventPos);
        } else if (newBlock instanceof BlockAir && oldBlock instanceof BlockChest) {
            if (eventPos.equals((Object)this.chestSolving)) {
                this.log("Chest removed; solved");
                this.chestSolved = true;
            } else {
                this.log("Chest Despawned");
                chestQueue.remove(eventPos);
            }
        }
    }

    @Override
    @SubscribeEvent
    protected void onWorldUnload(WorldEvent.Unload event) {
        chestQueue.clear();
    }

    @SubscribeEvent
    protected void onChat(ClientChatReceivedEvent event) {
        if (!this.enabled) {
            return;
        }
        String message = event.message.func_150260_c();
        if (message.equals("CHEST LOCKPICKED")) {
            this.chestSolved = true;
        }
    }

    static enum ChestFailure {
        NONE,
        NO_TOOL_IN;

    }

    static enum State {
        STARTING,
        FINDING_WALKABLE_BLOCKS,
        WALKING,
        WAITING,
        LOOKING,
        VERIFYING_ROTATION,
        CLICKING,
        ENDING;

    }
}

