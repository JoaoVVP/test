/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.jelly.mightyminerv2.util.helper;

import com.jelly.mightyminerv2.util.helper.Angle;
import com.jelly.mightyminerv2.util.helper.Target;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.client.Minecraft;

public class RotationConfiguration {
    private final Minecraft mc = Minecraft.func_71410_x();
    private Optional<Angle> from = Optional.empty();
    private Optional<Angle> to = Optional.empty();
    private Optional<Target> target;
    private Optional<Runnable> callback;
    private long time;
    private boolean easeBackToClientSide = false;
    private boolean followTarget = false;
    private RotationType rotationType = RotationType.CLIENT;
    private Ease easeFunction = Ease.values()[new Random().nextInt(Ease.values().length - 1)];
    private boolean randomness = false;

    public RotationConfiguration(Angle from, Angle to, long time, RotationType rotationType, Runnable callback) {
        this.from = Optional.of(from);
        this.to = Optional.ofNullable(to);
        this.target = Optional.of(new Target(to));
        this.time = time;
        this.rotationType = rotationType;
        this.callback = Optional.ofNullable(callback);
    }

    public RotationConfiguration(Angle from, Target target, long time, RotationType rotationType, Runnable callback) {
        this.from = Optional.of(from);
        this.time = time;
        this.target = Optional.ofNullable(target);
        this.rotationType = rotationType;
        this.callback = Optional.ofNullable(callback);
    }

    public RotationConfiguration(Angle to, long time, Runnable callback) {
        this.to = Optional.ofNullable(to);
        this.target = Optional.of(new Target(to));
        this.time = time;
        this.callback = Optional.ofNullable(callback);
    }

    public RotationConfiguration(Angle to, long time, RotationType rotationType, Runnable callback) {
        this.to = Optional.ofNullable(to);
        this.target = Optional.of(new Target(to));
        this.time = time;
        this.rotationType = rotationType;
        this.callback = Optional.ofNullable(callback);
    }

    public RotationConfiguration(Target target, long time, Runnable callback) {
        this.time = time;
        this.target = Optional.ofNullable(target);
        this.callback = Optional.ofNullable(callback);
    }

    public RotationConfiguration(Target target, long time, RotationType rotationType, Runnable callback) {
        this.time = time;
        this.target = Optional.ofNullable(target);
        this.rotationType = rotationType;
        this.callback = Optional.ofNullable(callback);
    }

    public RotationConfiguration setTarget(Target target) {
        this.target = Optional.of(target);
        return this;
    }

    public RotationConfiguration easeFunction(Ease ease) {
        this.easeFunction = ease;
        return this;
    }

    public Ease easeFunction() {
        return this.easeFunction;
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Minecraft mc() {
        return this.mc;
    }

    public Optional<Angle> from() {
        return this.from;
    }

    public Optional<Angle> to() {
        return this.to;
    }

    public Optional<Target> target() {
        return this.target;
    }

    public Optional<Runnable> callback() {
        return this.callback;
    }

    public long time() {
        return this.time;
    }

    public boolean easeBackToClientSide() {
        return this.easeBackToClientSide;
    }

    public boolean followTarget() {
        return this.followTarget;
    }

    public RotationType rotationType() {
        return this.rotationType;
    }

    public boolean randomness() {
        return this.randomness;
    }

    public RotationConfiguration from(Optional<Angle> from) {
        this.from = from;
        return this;
    }

    public RotationConfiguration to(Optional<Angle> to) {
        this.to = to;
        return this;
    }

    public RotationConfiguration target(Optional<Target> target) {
        this.target = target;
        return this;
    }

    public RotationConfiguration callback(Optional<Runnable> callback) {
        this.callback = callback;
        return this;
    }

    public RotationConfiguration time(long time) {
        this.time = time;
        return this;
    }

    public RotationConfiguration easeBackToClientSide(boolean easeBackToClientSide) {
        this.easeBackToClientSide = easeBackToClientSide;
        return this;
    }

    public RotationConfiguration followTarget(boolean followTarget) {
        this.followTarget = followTarget;
        return this;
    }

    public RotationConfiguration rotationType(RotationType rotationType) {
        this.rotationType = rotationType;
        return this;
    }

    public RotationConfiguration randomness(boolean randomness) {
        this.randomness = randomness;
        return this;
    }

    public static enum Ease {
        EASE_OUT_SINE(x -> Float.valueOf((float)Math.sin((double)x.floatValue() * Math.PI / 2.0))),
        EASE_IN_OUT_SINE(x -> Float.valueOf((float)(-(Math.cos((double)x.floatValue() * Math.PI) - 1.0) / 2.0))),
        EASE_OUT_QUAD(x -> Float.valueOf(1.0f - (1.0f - x.floatValue()) * (1.0f - x.floatValue()))),
        EASE_OUT_CUBIC(x -> Float.valueOf(1.0f - (1.0f - x.floatValue()) * (1.0f - x.floatValue()) * (1.0f - x.floatValue()))),
        EASE_OUT_CIRC(x -> Float.valueOf((float)Math.sqrt(1.0f - (x.floatValue() - 1.0f) * (x.floatValue() - 1.0f))));

        private final Function<Float, Float> easingFunction;

        private Ease(Function<Float, Float> easingFunction) {
            this.easingFunction = easingFunction;
        }

        public float invoke(float x) {
            return this.easingFunction.apply(Float.valueOf(x)).floatValue();
        }
    }

    public static enum RotationType {
        SERVER,
        CLIENT;

    }
}

