/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSet;
import java.util.SortedSet;

public interface DoubleSortedSet
extends DoubleSet,
SortedSet<Double>,
DoubleBidirectionalIterable {
    public DoubleBidirectionalIterator iterator(double var1);

    @Override
    public DoubleBidirectionalIterator iterator();

    public DoubleSortedSet subSet(double var1, double var3);

    public DoubleSortedSet headSet(double var1);

    public DoubleSortedSet tailSet(double var1);

    public DoubleComparator comparator();

    public double firstDouble();

    public double lastDouble();

    @Deprecated
    default public DoubleSortedSet subSet(Double from, Double to) {
        return this.subSet((double)from, (double)to);
    }

    @Deprecated
    default public DoubleSortedSet headSet(Double to) {
        return this.headSet((double)to);
    }

    @Deprecated
    default public DoubleSortedSet tailSet(Double from) {
        return this.tailSet((double)from);
    }

    @Override
    @Deprecated
    default public Double first() {
        return this.firstDouble();
    }

    @Override
    @Deprecated
    default public Double last() {
        return this.lastDouble();
    }
}

