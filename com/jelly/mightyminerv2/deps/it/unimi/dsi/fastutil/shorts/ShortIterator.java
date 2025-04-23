/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortConsumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public interface ShortIterator
extends Iterator<Short> {
    public short nextShort();

    @Override
    @Deprecated
    default public Short next() {
        return this.nextShort();
    }

    default public void forEachRemaining(ShortConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextShort());
        }
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Short> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextShort();
        }
        return n - i - 1;
    }
}

