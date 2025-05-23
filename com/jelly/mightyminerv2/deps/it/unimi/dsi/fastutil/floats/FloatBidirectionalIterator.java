/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface FloatBidirectionalIterator
extends FloatIterator,
ObjectBidirectionalIterator<Float> {
    public float previousFloat();

    @Override
    @Deprecated
    default public Float previous() {
        return Float.valueOf(this.previousFloat());
    }

    @Override
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousFloat();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return FloatIterator.super.skip(n);
    }
}

