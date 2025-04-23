/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteConsumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public interface ByteIterator
extends Iterator<Byte> {
    public byte nextByte();

    @Override
    @Deprecated
    default public Byte next() {
        return this.nextByte();
    }

    default public void forEachRemaining(ByteConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextByte());
        }
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Byte> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextByte();
        }
        return n - i - 1;
    }
}

