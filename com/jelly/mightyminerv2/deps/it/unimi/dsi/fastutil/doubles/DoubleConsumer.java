/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface DoubleConsumer
extends Consumer<Double>,
java.util.function.DoubleConsumer {
    @Override
    @Deprecated
    default public void accept(Double t) {
        this.accept(t.doubleValue());
    }

    @Override
    default public DoubleConsumer andThen(java.util.function.DoubleConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }

    @Override
    @Deprecated
    default public Consumer<Double> andThen(Consumer<? super Double> after) {
        return Consumer.super.andThen(after);
    }
}

