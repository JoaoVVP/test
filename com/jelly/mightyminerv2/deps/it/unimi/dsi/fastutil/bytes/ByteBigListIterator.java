/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;

public interface ByteBigListIterator
extends ByteBidirectionalIterator,
BigListIterator<Byte> {
    @Override
    default public void set(byte k) {
        throw new UnsupportedOperationException();
    }

    @Override
    default public void add(byte k) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public void set(Byte k) {
        this.set((byte)k);
    }

    @Override
    @Deprecated
    default public void add(Byte k) {
        this.add((byte)k);
    }

    default public long skip(long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextByte();
        }
        return n - i - 1L;
    }

    default public long back(long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousByte();
        }
        return n - i - 1L;
    }

    @Override
    default public int skip(int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}

