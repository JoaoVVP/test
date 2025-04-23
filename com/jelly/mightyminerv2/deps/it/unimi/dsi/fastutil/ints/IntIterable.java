/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntIterable
extends Iterable<Integer> {
    public IntIterator iterator();

    default public void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextInt());
        }
    }

    @Override
    @Deprecated
    default public void forEach(Consumer<? super Integer> action) {
        this.forEach(action::accept);
    }
}

