/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatConsumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public interface FloatIterator
extends Iterator<Float> {
    public float nextFloat();

    @Override
    @Deprecated
    default public Float next() {
        return Float.valueOf(this.nextFloat());
    }

    default public void forEachRemaining(FloatConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextFloat());
        }
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Float> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextFloat();
        }
        return n - i - 1;
    }
}

