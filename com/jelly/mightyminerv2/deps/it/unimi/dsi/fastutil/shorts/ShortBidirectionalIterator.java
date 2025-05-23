/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;

public interface ShortBidirectionalIterator
extends ShortIterator,
ObjectBidirectionalIterator<Short> {
    public short previousShort();

    @Override
    @Deprecated
    default public Short previous() {
        return this.previousShort();
    }

    @Override
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousShort();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return ShortIterator.super.skip(n);
    }
}

