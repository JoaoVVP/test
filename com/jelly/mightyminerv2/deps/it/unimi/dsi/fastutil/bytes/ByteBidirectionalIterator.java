/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface ByteBidirectionalIterator
extends ByteIterator,
ObjectBidirectionalIterator<Byte> {
    public byte previousByte();

    @Override
    @Deprecated
    default public Byte previous() {
        return this.previousByte();
    }

    @Override
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousByte();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return ByteIterator.super.skip(n);
    }
}

