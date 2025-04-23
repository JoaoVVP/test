/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;
import java.util.Set;

public interface ShortSet
extends ShortCollection,
Set<Short> {
    @Override
    public ShortIterator iterator();

    public boolean remove(short var1);

    @Override
    @Deprecated
    default public boolean remove(Object o) {
        return ShortCollection.super.remove(o);
    }

    @Override
    @Deprecated
    default public boolean add(Short o) {
        return ShortCollection.super.add(o);
    }

    @Override
    @Deprecated
    default public boolean contains(Object o) {
        return ShortCollection.super.contains(o);
    }

    @Override
    @Deprecated
    default public boolean rem(short k) {
        return this.remove(k);
    }
}

