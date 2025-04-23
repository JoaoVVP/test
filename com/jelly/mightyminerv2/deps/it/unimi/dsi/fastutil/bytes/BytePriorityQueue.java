/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.PriorityQueue;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteComparator;

public interface BytePriorityQueue
extends PriorityQueue<Byte> {
    @Override
    public void enqueue(byte var1);

    public byte dequeueByte();

    public byte firstByte();

    default public byte lastByte() {
        throw new UnsupportedOperationException();
    }

    public ByteComparator comparator();

    @Override
    @Deprecated
    default public void enqueue(Byte x) {
        this.enqueue((byte)x);
    }

    @Override
    @Deprecated
    default public Byte dequeue() {
        return this.dequeueByte();
    }

    @Override
    @Deprecated
    default public Byte first() {
        return this.firstByte();
    }

    @Override
    @Deprecated
    default public Byte last() {
        return this.lastByte();
    }
}

