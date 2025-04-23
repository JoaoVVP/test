/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.feature.impl.Pathfinder;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.util.BlockUtil;
import com.jelly.mightyminerv2.util.EntityUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.helper.Clock;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import kotlin.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoMobKiller
extends AbstractFeature {
    private static AutoMobKiller instance;
    private State state = State.STARTING;
    private MKError mkError = MKError.NONE;
    private final Clock shutdownTimer = new Clock();
    private final Clock queueTimer = new Clock();
    private final Clock recheckTimer = new Clock();
    private final Set<EntityLivingBase> mobQueue = new HashSet<EntityLivingBase>();
    private Optional<EntityLivingBase> targetMob = Optional.empty();
    private Optional<Vec3> entityLastPosition = Optional.empty();
    private final Set<String> mobToKill = new HashSet<String>();
    private Map<EntityLivingBase, Pair<String, BlockPos>> debug = new HashMap<EntityLivingBase, Pair<String, BlockPos>>();
    int pathRetry = 0;

    public static AutoMobKiller getInstance() {
        if (instance == null) {
            instance = new AutoMobKiller();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "MobKiller";
    }

    public void start(Collection<String> mobsToKill, String weaponName) {
        if (!InventoryUtil.holdItem(weaponName)) {
            this.error("Could not hold MobKiller Weapon");
            this.stop(MKError.NO_ENTITIES);
            return;
        }
        this.mobToKill.addAll(mobsToKill);
        this.mkError = MKError.NONE;
        this.enabled = true;
        this.debug.clear();
        this.start();
        this.note("Mobkiller started for " + mobsToKill);
        this.log("Started");
    }

    @Override
    public void stop() {
        if (!this.enabled) {
            return;
        }
        this.enabled = false;
        this.mobToKill.clear();
        this.timer.reset();
        this.shutdownTimer.reset();
        this.targetMob = Optional.empty();
        this.resetStatesAfterStop();
        Pathfinder.getInstance().stop();
        this.log("stopped");
    }

    public void stop(MKError error) {
        this.mkError = error;
        this.stop();
    }

    @Override
    public void resetStatesAfterStop() {
        this.state = State.STARTING;
    }

    public boolean succeeded() {
        return !this.enabled && this.mkError == MKError.NONE;
    }

    public MKError getMkError() {
        return this.mkError;
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        if (this.shutdownTimer.isScheduled() && this.shutdownTimer.passed()) {
            this.stop(MKError.NO_ENTITIES);
            this.log("Entities did not spawn");
            return;
        }
        if (this.queueTimer.passed()) {
            this.log("Queue celared");
            this.mobQueue.clear();
            this.queueTimer.schedule(MightyMinerConfig.devMKillTimer);
        }
        switch (this.state) {
            case STARTING: {
                this.targetMob.ifPresent(this.mobQueue::add);
                this.changeState(State.FINDING_MOB, 0);
                this.recheckTimer.reset();
                this.log("Starting");
            }
            case FINDING_MOB: {
                if (!this.recheckTimer.isScheduled() || this.recheckTimer.passed()) {
                    List<EntityLivingBase> mobs = EntityUtil.getEntities(this.mobToKill, this.mobQueue);
                    if (mobs.isEmpty()) {
                        if (!this.shutdownTimer.isScheduled()) {
                            this.log("Cannot find mobs. Starting a 10 second timer");
                            this.shutdownTimer.schedule(10000L);
                        }
                        return;
                    }
                    if (this.shutdownTimer.isScheduled()) {
                        this.shutdownTimer.reset();
                    }
                    EntityLivingBase best = null;
                    for (EntityLivingBase mob : mobs) {
                        BlockPos mobPos = EntityUtil.getBlockStandingOn((Entity)mob);
                        if (!BlockUtil.canStandOn(mobPos)) continue;
                        best = mob;
                        break;
                    }
                    if (best == null) {
                        this.log("Didnt find a mob that has a valid position. ");
                        this.changeState(State.STARTING, 0);
                        return;
                    }
                    if (!this.targetMob.isPresent() || !this.targetMob.get().equals((Object)best)) {
                        this.targetMob = Optional.of(best);
                        this.entityLastPosition = Optional.of(best.func_174791_d());
                        Pathfinder.getInstance().stopAndRequeue(EntityUtil.getBlockStandingOn((Entity)this.targetMob.get()));
                    }
                    this.recheckTimer.schedule(MightyMinerConfig.devMKillTimer);
                }
                if (!this.targetMob.isPresent() || !this.entityLastPosition.isPresent()) {
                    this.stop(MKError.NO_ENTITIES);
                    this.log("no target mob || no last position saved");
                    return;
                }
                if (!Pathfinder.getInstance().isRunning()) {
                    this.log("Pathfinder wasnt enabled. starting");
                    Pathfinder.getInstance().setSprintState(MightyMinerConfig.commMobKillerSprint);
                    Pathfinder.getInstance().setInterpolationState(MightyMinerConfig.commMobKillerInterpolate);
                    Pathfinder.getInstance().start();
                }
                if (PlayerUtil.getNextTickPosition().func_72436_e(this.targetMob.get().func_174791_d()) < 8.0 && this.mc.field_71439_g.func_70685_l((Entity)this.targetMob.get())) {
                    this.log("Looking at mob");
                    this.changeState(State.LOOKING_AT_MOB, 0);
                    return;
                }
                if (!this.targetMob.get().func_70089_S()) {
                    Pathfinder.getInstance().stop();
                    this.changeState(State.STARTING, 0);
                    this.log("Targetmob isnt alive");
                    return;
                }
                if (this.targetMob.get().func_174791_d().func_72436_e(this.entityLastPosition.get()) > 9.0) {
                    if (++this.pathRetry > 3) {
                        this.changeState(State.STARTING, 0);
                        this.log("target mob moved away. repathing");
                        this.pathRetry = 0;
                        return;
                    }
                    this.log("Repathing");
                    this.entityLastPosition = Optional.of(this.targetMob.get().func_174791_d());
                    Pathfinder.getInstance().stopAndRequeue(EntityUtil.getBlockStandingOn((Entity)this.targetMob.get()));
                    return;
                }
                if (Pathfinder.getInstance().isRunning()) break;
                this.log("Pathfinder not enabled");
                this.changeState(State.STARTING, 0);
                return;
            }
            case LOOKING_AT_MOB: {
                if (!Pathfinder.getInstance().isRunning()) {
                    RotationHandler.getInstance().easeTo(new RotationConfiguration(new Target((Entity)this.targetMob.get()), 400L, null));
                    this.changeState(State.KILLING_MOB, 0);
                    this.log("Rotating");
                }
            }
            case KILLING_MOB: {
                if (!Objects.equals(this.mc.field_71476_x.field_72308_g, this.targetMob.get())) {
                    if (this.mc.field_71439_g.func_70068_e((Entity)this.targetMob.get()) < 9.0 && Pathfinder.getInstance().isRunning()) {
                        Pathfinder.getInstance().stop();
                        return;
                    }
                    if (!Pathfinder.getInstance().isRunning() && !RotationHandler.getInstance().isEnabled()) {
                        this.changeState(State.STARTING, 0);
                    }
                    return;
                }
                KeyBindUtil.leftClick();
                RotationHandler.getInstance().stop();
                this.changeState(State.STARTING, 0);
            }
        }
    }

    @Override
    @SubscribeEvent
    protected void onRender(RenderWorldLastEvent event) {
        if (!this.enabled || !this.targetMob.isPresent()) {
            return;
        }
        RenderUtil.drawBox(this.targetMob.get().func_174813_aQ(), new Color(255, 255, 255, 200));
    }

    private void changeState(State state, int time) {
        this.state = state;
        this.timer.schedule(time);
    }

    public static enum MKError {
        NONE,
        NO_ENTITIES;

    }

    static enum State {
        STARTING,
        FINDING_MOB,
        WALKING_TO_MOB,
        WAITING_FOR_MOB,
        LOOKING_AT_MOB,
        KILLING_MOB;

    }
}

