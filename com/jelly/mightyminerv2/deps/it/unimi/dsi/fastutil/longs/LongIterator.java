/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface LongIterator
extends PrimitiveIterator.OfLong {
    @Override
    public long nextLong();

    @Override
    @Deprecated
    default public Long next() {
        return this.nextLong();
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Long> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextLong();
        }
        return n - i - 1;
    }
}

