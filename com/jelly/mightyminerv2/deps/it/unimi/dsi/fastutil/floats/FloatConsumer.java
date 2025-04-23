/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface FloatConsumer
extends Consumer<Float>,
DoubleConsumer {
    @Override
    public void accept(float var1);

    @Override
    @Deprecated
    default public void accept(double t) {
        this.accept(SafeMath.safeDoubleToFloat(t));
    }

    @Override
    @Deprecated
    default public void accept(Float t) {
        this.accept(t.floatValue());
    }

    default public FloatConsumer andThen(FloatConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }

    @Override
    @Deprecated
    default public FloatConsumer andThen(DoubleConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }

    @Override
    @Deprecated
    default public Consumer<Float> andThen(Consumer<? super Float> after) {
        return Consumer.super.andThen(after);
    }
}

