/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanIterator;
import java.util.Objects;
import java.util.function.Consumer;

public interface BooleanIterable
extends Iterable<Boolean> {
    public BooleanIterator iterator();

    default public void forEach(BooleanConsumer action) {
        Objects.requireNonNull(action);
        BooleanIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextBoolean());
        }
    }

    @Override
    @Deprecated
    default public void forEach(Consumer<? super Boolean> action) {
        this.forEach(action::accept);
    }
}

