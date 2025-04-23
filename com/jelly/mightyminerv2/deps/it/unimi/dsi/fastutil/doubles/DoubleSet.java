/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleIterator;
import java.util.Set;

public interface DoubleSet
extends DoubleCollection,
Set<Double> {
    @Override
    public DoubleIterator iterator();

    public boolean remove(double var1);

    @Override
    @Deprecated
    default public boolean remove(Object o) {
        return DoubleCollection.super.remove(o);
    }

    @Override
    @Deprecated
    default public boolean add(Double o) {
        return DoubleCollection.super.add(o);
    }

    @Override
    @Deprecated
    default public boolean contains(Object o) {
        return DoubleCollection.super.contains(o);
    }

    @Override
    @Deprecated
    default public boolean rem(double k) {
        return this.remove(k);
    }
}

