/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface IntConsumer
extends Consumer<Integer>,
java.util.function.IntConsumer {
    @Override
    @Deprecated
    default public void accept(Integer t) {
        this.accept(t.intValue());
    }

    @Override
    default public IntConsumer andThen(java.util.function.IntConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }

    @Override
    @Deprecated
    default public Consumer<Integer> andThen(Consumer<? super Integer> after) {
        return Consumer.super.andThen(after);
    }
}

