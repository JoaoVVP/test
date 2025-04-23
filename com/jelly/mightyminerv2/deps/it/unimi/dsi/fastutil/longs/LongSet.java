/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.Set;

public interface LongSet
extends LongCollection,
Set<Long> {
    @Override
    public LongIterator iterator();

    public boolean remove(long var1);

    @Override
    @Deprecated
    default public boolean remove(Object o) {
        return LongCollection.super.remove(o);
    }

    @Override
    @Deprecated
    default public boolean add(Long o) {
        return LongCollection.super.add(o);
    }

    @Override
    @Deprecated
    default public boolean contains(Object o) {
        return LongCollection.super.contains(o);
    }

    @Override
    @Deprecated
    default public boolean rem(long k) {
        return this.remove(k);
    }
}

