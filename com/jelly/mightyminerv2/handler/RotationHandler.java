/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.jelly.mightyminerv2.handler;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.MotionUpdateEvent;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.helper.Angle;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RotationHandler {
    private static final Logger log = LoggerFactory.getLogger(RotationHandler.class);
    private static RotationHandler instance;
    private final Queue<RotationConfiguration> rotations = new LinkedList<RotationConfiguration>();
    private final Minecraft mc = Minecraft.func_71410_x();
    private boolean enabled;
    private long startTime;
    private long endTime;
    private final Angle startRotation = new Angle(0.0f, 0.0f);
    private Target target = new Target(new Angle(0.0f, 0.0f));
    private float lastBezierYaw = 0.0f;
    private float lastBezierPitch = 0.0f;
    private float serverSideYaw = 0.0f;
    private float serverSidePitch = 0.0f;
    private int randomMultiplier1 = 1;
    private int randomMultiplier2 = 1;
    private boolean followingTarget = false;
    private boolean stopRequested = false;
    private RotationConfiguration configuration;
    private final Random random = new Random();

    public static RotationHandler getInstance() {
        if (instance == null) {
            instance = new RotationHandler();
        }
        return instance;
    }

    public RotationHandler queueRotation(RotationConfiguration ... configs) {
        this.rotations.addAll(Arrays.asList(configs));
        return instance;
    }

    public void start() {
        if (this.rotations.isEmpty() || this.enabled) {
            return;
        }
        this.easeTo(this.rotations.poll());
    }

    public void easeTo(RotationConfiguration configuration) {
        this.configuration = configuration;
        this.startTime = System.currentTimeMillis();
        this.startRotation.setRotation(configuration.from().orElse(AngleUtil.getPlayerAngle()));
        this.target = configuration.target().get();
        Angle change = AngleUtil.getNeededChange(this.startRotation, this.target.getTargetAngle());
        this.endTime = this.startTime + this.getTime(this.pythagoras(change.getYaw(), change.getPitch()), configuration.time());
        this.randomMultiplier2 = this.random.nextBoolean() ? MightyMinerConfig.cost4 : -MightyMinerConfig.cost4;
        this.randomMultiplier1 = this.randomMultiplier2;
        this.lastBezierYaw = 0.0f;
        this.lastBezierPitch = 0.0f;
        if (configuration.rotationType() == RotationConfiguration.RotationType.SERVER) {
            if (this.serverSideYaw == 0.0f && this.serverSidePitch == 0.0f) {
                this.serverSideYaw = this.mc.field_71439_g.field_70177_z;
                this.serverSidePitch = this.mc.field_71439_g.field_70125_A;
            } else {
                this.startRotation.setYaw(AngleUtil.get360RotationYaw(this.serverSideYaw));
                this.startRotation.setPitch(this.serverSidePitch);
            }
        }
        this.stopRequested = false;
        this.enabled = true;
    }

    private void reset() {
        if (this.stopRequested) {
            this.configuration = null;
            this.target = null;
            this.endTime = 0L;
            this.startTime = 0L;
            this.lastBezierPitch = 0.0f;
            this.lastBezierYaw = 0.0f;
            this.serverSidePitch = 0.0f;
            this.serverSideYaw = 0.0f;
        }
        this.enabled = false;
        this.followingTarget = false;
        this.stopRequested = false;
    }

    public void stop() {
        this.rotations.clear();
        this.stopRequested = true;
        this.enabled = false;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!this.enabled || this.configuration == null || this.configuration.rotationType() != RotationConfiguration.RotationType.CLIENT) {
            return;
        }
        Angle bezierAngle = this.getBezierAngle();
        this.mc.field_71439_g.field_70177_z += bezierAngle.getYaw() - this.lastBezierYaw;
        this.mc.field_71439_g.field_70125_A += bezierAngle.getPitch() - this.lastBezierPitch;
        this.lastBezierYaw = bezierAngle.getYaw();
        this.lastBezierPitch = bezierAngle.getPitch();
        if (System.currentTimeMillis() > this.endTime || this.stopRequested) {
            this.handleRotationEnd();
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onMotionUpdate(MotionUpdateEvent event) {
        if (!this.enabled || this.configuration == null || this.configuration.rotationType() != RotationConfiguration.RotationType.SERVER) {
            return;
        }
        Angle bezierAngle = this.getBezierAngle();
        this.serverSideYaw += bezierAngle.getYaw() - this.lastBezierYaw;
        this.serverSidePitch += bezierAngle.getPitch() - this.lastBezierPitch;
        event.yaw = this.serverSideYaw;
        event.pitch = this.serverSidePitch;
        this.lastBezierYaw = bezierAngle.getYaw();
        this.lastBezierPitch = bezierAngle.getPitch();
        if (System.currentTimeMillis() > this.endTime || this.stopRequested) {
            this.handleRotationEnd();
        }
    }

    private Angle getBezierAngle() {
        float totalTime = this.endTime - this.startTime;
        float timeProgress = Math.min(totalTime, (float)(System.currentTimeMillis() - this.startTime)) / totalTime;
        float rotationProgress = this.configuration.easeFunction().invoke(timeProgress);
        Angle bezierEnd = AngleUtil.getNeededChange(this.startRotation, this.target.getTargetAngle());
        Angle control1 = new Angle(bezierEnd.getYaw() * 0.05f * (float)this.randomMultiplier1, bezierEnd.getYaw() * 0.1f * (float)this.randomMultiplier2);
        Angle control2 = new Angle(bezierEnd.getYaw() - bezierEnd.getYaw() * 0.05f * (float)this.randomMultiplier2, bezierEnd.getPitch() - bezierEnd.getYaw() * 0.1f * (float)this.randomMultiplier1);
        double bezierYawSoFar = this.bezier(rotationProgress, control1.getYaw(), control2.getYaw(), bezierEnd.getYaw());
        double bezierPitchSoFar = this.bezier(rotationProgress, control1.getPitch(), control2.getPitch(), bezierEnd.getPitch());
        return new Angle((float)bezierYawSoFar, (float)bezierPitchSoFar);
    }

    private double bezier(float t, float c1, float c2, float end) {
        return 3.0 * Math.pow(1.0f - t, 2.0) * (double)t * (double)c1 + (double)(3.0f * (1.0f - t)) * Math.pow(t, 2.0) * (double)c2 + Math.pow(t, 3.0) * (double)end;
    }

    private void handleRotationEnd() {
        if (!this.stopRequested) {
            if (this.configuration.followTarget()) {
                System.out.println("Following Target");
                this.easeTo(this.configuration);
                this.followingTarget = true;
                return;
            }
            this.configuration.callback().ifPresent(Runnable::run);
            if (!this.rotations.isEmpty()) {
                this.easeTo(this.rotations.poll());
                return;
            }
            if (this.configuration.rotationType() == RotationConfiguration.RotationType.SERVER && this.configuration.easeBackToClientSide()) {
                RotationConfiguration newConf = new RotationConfiguration(AngleUtil.getPlayerAngle(), this.configuration.time(), RotationConfiguration.RotationType.SERVER, () -> {});
                this.easeTo(newConf);
                return;
            }
        }
        this.reset();
    }

    private double pythagoras(float yaw, float pitch) {
        return Math.sqrt(yaw * yaw + pitch * pitch);
    }

    private long getTime(double pythagoras, long time) {
        if (time <= 0L) {
            return 1L;
        }
        if (pythagoras < 25.0) {
            return (long)((double)time * 0.65);
        }
        if (pythagoras < 45.0) {
            return (long)((double)time * 0.77);
        }
        if (pythagoras < 80.0) {
            return (long)((double)time * 0.9);
        }
        if (pythagoras > 100.0) {
            return (long)((double)time * 1.1);
        }
        return (long)((double)time * 1.0);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isFollowingTarget() {
        return this.followingTarget;
    }

    public void stopFollowingTarget() {
        if (this.configuration != null) {
            this.configuration.followTarget(false);
        }
    }

    public RotationConfiguration getConfiguration() {
        return this.configuration;
    }
}

