/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook$EnumFlags
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.feature.impl.Pathfinder;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.BlockUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import com.jelly.mightyminerv2.util.helper.route.Route;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RouteNavigator
extends AbstractFeature {
    private static RouteNavigator instance;
    private Route routeToFollow;
    private int currentRouteIndex = -1;
    private int targetRouteIndex = -1;
    private State state = State.STARTING;
    private boolean isQueued = false;
    private NavError navError = NavError.NONE;
    private RotationConfiguration.RotationType rotationType = RotationConfiguration.RotationType.CLIENT;
    private Vec3 rotationTarget = null;

    public static RouteNavigator getInstance() {
        if (instance == null) {
            instance = new RouteNavigator();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "RouteNavigator";
    }

    @Override
    public void resetStatesAfterStop() {
        this.state = State.STARTING;
        this.rotationType = RotationConfiguration.RotationType.CLIENT;
        KeyBindUtil.releaseAllExcept(new KeyBinding[0]);
        RotationHandler.getInstance().stop();
    }

    @Override
    public boolean shouldNotCheckForFailsafe() {
        return this.state == State.AOTV_VERIFY;
    }

    public void queueRoute(Route routeToFollow) {
        if (this.enabled) {
            return;
        }
        this.routeToFollow = routeToFollow;
        this.currentRouteIndex = -1;
        this.targetRouteIndex = -1;
        this.isQueued = true;
    }

    public void goTo(int index) {
        if (this.routeToFollow == null || this.routeToFollow.isEmpty()) {
            this.error("No Route Was Selected or its empty.");
            return;
        }
        this.targetRouteIndex = index;
        this.currentRouteIndex = this.getCurrentIndex(PlayerUtil.getBlockStandingOn()) - 1;
        this.normalizeIndices();
        this.navError = NavError.NONE;
        this.enabled = true;
        this.start();
    }

    public void gotoNext() {
        this.goTo(this.targetRouteIndex + 1);
    }

    public void start(Route routeToFollow) {
        this.routeToFollow = routeToFollow;
        this.enabled = true;
        this.targetRouteIndex = -1;
        this.normalizeIndices();
        this.currentRouteIndex = -1;
        this.navError = NavError.NONE;
        this.start();
        this.send("Enabling RouteNavigator.");
    }

    @Override
    public void pause() {
        this.enabled = false;
        this.timer.reset();
        this.resetStatesAfterStop();
        this.send("Pausing RouteNavigator");
    }

    public void setRotationType(RotationConfiguration.RotationType type) {
        this.rotationType = type;
    }

    @Override
    public void stop() {
        if (!this.enabled) {
            return;
        }
        this.enabled = false;
        this.isQueued = false;
        this.routeToFollow = null;
        this.timer.reset();
        this.targetRouteIndex = -1;
        this.currentRouteIndex = -1;
        this.rotationTarget = null;
        this.resetStatesAfterStop();
        this.send("RouteNavigator Stopped");
    }

    public void stop(NavError error) {
        this.navError = error;
        this.stop();
    }

    private void normalizeIndices() {
        this.targetRouteIndex = this.normalizeIndex(this.targetRouteIndex);
        this.currentRouteIndex = this.normalizeIndex(this.currentRouteIndex);
        if (this.targetRouteIndex < this.currentRouteIndex) {
            this.targetRouteIndex += this.routeToFollow.size();
        }
    }

    private int normalizeIndex(int index) {
        return (index + this.routeToFollow.size()) % this.routeToFollow.size();
    }

    private int getLookTime(RouteWaypoint waypoint) {
        if (this.rotationType == RotationConfiguration.RotationType.SERVER) {
            return MightyMinerConfig.delayAutoAotvServerRotation;
        }
        if (waypoint.getTransportMethod().ordinal() == 0) {
            return MightyMinerConfig.delayAutoAotvLookDelay;
        }
        return MightyMinerConfig.delayAutoAotvEtherwarpLookDelay;
    }

    public int getCurrentIndex(BlockPos playerBlock) {
        int index = this.routeToFollow.indexOf(new RouteWaypoint(playerBlock, TransportMethod.ETHERWARP));
        if (index != -1) {
            return index;
        }
        return this.routeToFollow.getClosest(playerBlock).map(routeWaypoint -> this.routeToFollow.indexOf((RouteWaypoint)routeWaypoint)).orElse(-1);
    }

    public boolean succeeded() {
        return !this.enabled && this.navError == NavError.NONE;
    }

    public NavError getNavError() {
        return this.navError;
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        switch (this.state) {
            case STARTING: {
                this.swapState(State.DETECT_ROUTE, 0);
                break;
            }
            case DETECT_ROUTE: {
                if (this.currentRouteIndex++ == this.targetRouteIndex) {
                    this.swapState(State.END_VERIFY, 0);
                    return;
                }
                if (this.routeToFollow.get(this.currentRouteIndex).getTransportMethod() == TransportMethod.WALK) {
                    this.swapState(State.WALK, 0);
                } else {
                    this.swapState(State.ROTATION, 0);
                }
                this.log("Going To Index: " + this.currentRouteIndex);
                break;
            }
            case ROTATION: {
                RouteWaypoint nextPoint = this.routeToFollow.get(this.currentRouteIndex);
                this.rotationTarget = BlockUtil.getClosestVisibleSidePos(nextPoint.toBlockPos());
                RotationConfiguration config = new RotationConfiguration(new Target(this.rotationTarget), (long)this.getLookTime(nextPoint), this.rotationType, null).followTarget(true);
                RotationHandler.getInstance().easeTo(config);
                this.swapState(State.ROTATION_VERIFY, 2000);
                this.log("Rotating to " + this.rotationTarget);
                break;
            }
            case ROTATION_VERIFY: {
                if (this.timer.isScheduled() && this.timer.passed()) {
                    this.error("Could not look in time. Disabling.");
                    this.stop(NavError.TIME_FAIL);
                    return;
                }
                if (!AngleUtil.isLookingAt(this.rotationTarget, 0.5f) && !RotationHandler.getInstance().isFollowingTarget()) {
                    return;
                }
                System.out.println("IsLookingAt: " + AngleUtil.isLookingAtDebug(this.rotationTarget, 0.5f));
                System.out.println("Following: " + RotationHandler.getInstance().isFollowingTarget());
                int sneakTime = 0;
                RouteWaypoint target = this.routeToFollow.get(this.currentRouteIndex);
                if (target.getTransportMethod() == TransportMethod.ETHERWARP && !this.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74311_E, true);
                    sneakTime = 250;
                }
                this.swapState(State.AOTV, sneakTime);
                break;
            }
            case AOTV: {
                if (this.timer.isScheduled() && !this.timer.passed()) {
                    return;
                }
                if (this.routeToFollow.get(this.currentRouteIndex + 1).getTransportMethod() != TransportMethod.ETHERWARP) {
                    KeyBindUtil.setKeyBindState(this.mc.field_71474_y.field_74311_E, false);
                }
                KeyBindUtil.rightClick();
                System.out.println("clicked");
                this.swapState(State.AOTV_VERIFY, 2000);
                break;
            }
            case AOTV_VERIFY: {
                if (!this.timer.isScheduled() || !this.timer.passed()) break;
                this.error("Did not receive teleport packet in time. Disabling");
                this.stop(NavError.TIME_FAIL);
                return;
            }
            case WALK: {
                BlockPos source = this.routeToFollow.get(this.currentRouteIndex).toBlockPos();
                if (this.currentRouteIndex == 0) {
                    this.log("queued first");
                    BlockPos p = PlayerUtil.getBlockStandingOn();
                    Pathfinder.getInstance().queue(p, source);
                }
                if (this.currentRouteIndex + 1 <= this.targetRouteIndex) {
                    this.log("queued next");
                    RouteWaypoint target = this.routeToFollow.get(this.currentRouteIndex + 1);
                    Pathfinder.getInstance().queue(source, target.toBlockPos());
                }
                if (!Pathfinder.getInstance().isRunning()) {
                    this.log("Started");
                    Pathfinder.getInstance().setInterpolationState(true);
                    Pathfinder.getInstance().start();
                }
                this.swapState(State.WALK_VERIFY, 2000);
                this.log("Walking");
                break;
            }
            case WALK_VERIFY: {
                BlockPos targetPos = this.routeToFollow.get(this.currentRouteIndex).toBlockPos();
                if (Pathfinder.getInstance().completedPathTo(targetPos) || !Pathfinder.getInstance().isRunning() && Pathfinder.getInstance().succeeded() || PlayerUtil.getBlockStandingOn().equals((Object)targetPos)) {
                    this.log("Completed path. going to next");
                    this.swapState(State.STARTING, 0);
                    this.log("Done Walking");
                    return;
                }
                if (!Pathfinder.getInstance().failed()) break;
                this.error("Pathfinding failed");
                this.stop(NavError.PATHFIND_FAILED);
                return;
            }
            case END_VERIFY: {
                if (this.isQueued) {
                    this.pause();
                    break;
                }
                this.stop();
            }
        }
    }

    @Override
    @SubscribeEvent
    protected void onPacketReceive(PacketEvent.Received event) {
        if (!this.enabled) {
            return;
        }
        if (this.state != State.AOTV_VERIFY) {
            return;
        }
        if (!(event.packet instanceof S08PacketPlayerPosLook)) {
            return;
        }
        this.swapState(State.STARTING, 0);
        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.packet;
        RotationHandler.getInstance().stop();
        Vec3 playerPos = this.mc.field_71439_g.func_174791_d();
        Vec3 pos = new Vec3(packet.func_148932_c() + (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X) ? playerPos.field_72450_a : 0.0), packet.func_148928_d() + (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y) ? playerPos.field_72448_b : 0.0), packet.func_148933_e() + (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z) ? playerPos.field_72449_c : 0.0));
        if (pos.func_72438_d(this.routeToFollow.get(this.currentRouteIndex).toVec3()) > 6.0) {
            this.swapState(State.ROTATION, 0);
        }
    }

    @Override
    @SubscribeEvent
    protected void onRender(RenderWorldLastEvent event) {
        if (!this.isQueued) {
            return;
        }
        this.routeToFollow.drawRoute();
    }

    public void swapState(State toState, int delay) {
        this.state = toState;
        this.timer.schedule(delay);
    }

    public static enum NavError {
        NONE,
        TIME_FAIL,
        PATHFIND_FAILED;

    }

    static enum State {
        STARTING,
        DETECT_ROUTE,
        ROTATION,
        ROTATION_VERIFY,
        AOTV,
        AOTV_VERIFY,
        WALK,
        WALK_VERIFY,
        END_VERIFY;

    }
}

