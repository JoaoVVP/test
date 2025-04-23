/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;

public interface BooleanBigListIterator
extends BooleanBidirectionalIterator,
BigListIterator<Boolean> {
    @Override
    default public void set(boolean k) {
        throw new UnsupportedOperationException();
    }

    @Override
    default public void add(boolean k) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public void set(Boolean k) {
        this.set((boolean)k);
    }

    @Override
    @Deprecated
    default public void add(Boolean k) {
        this.add((boolean)k);
    }

    default public long skip(long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextBoolean();
        }
        return n - i - 1L;
    }

    default public long back(long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousBoolean();
        }
        return n - i - 1L;
    }

    @Override
    default public int skip(int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}

