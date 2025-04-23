/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public interface BooleanIterator
extends Iterator<Boolean> {
    public boolean nextBoolean();

    @Override
    @Deprecated
    default public Boolean next() {
        return this.nextBoolean();
    }

    default public void forEachRemaining(BooleanConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextBoolean());
        }
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Boolean> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextBoolean();
        }
        return n - i - 1;
    }
}

