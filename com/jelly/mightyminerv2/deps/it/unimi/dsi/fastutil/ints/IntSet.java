/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.Set;

public interface IntSet
extends IntCollection,
Set<Integer> {
    @Override
    public IntIterator iterator();

    public boolean remove(int var1);

    @Override
    @Deprecated
    default public boolean remove(Object o) {
        return IntCollection.super.remove(o);
    }

    @Override
    @Deprecated
    default public boolean add(Integer o) {
        return IntCollection.super.add(o);
    }

    @Override
    @Deprecated
    default public boolean contains(Object o) {
        return IntCollection.super.contains(o);
    }

    @Override
    @Deprecated
    default public boolean rem(int k) {
        return this.remove(k);
    }
}

