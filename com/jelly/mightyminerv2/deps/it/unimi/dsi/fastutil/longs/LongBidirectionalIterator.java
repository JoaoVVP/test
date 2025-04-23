/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface LongBidirectionalIterator
extends LongIterator,
ObjectBidirectionalIterator<Long> {
    public long previousLong();

    @Override
    @Deprecated
    default public Long previous() {
        return this.previousLong();
    }

    @Override
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousLong();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return LongIterator.super.skip(n);
    }
}

