/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;

@FunctionalInterface
public interface DoubleComparator
extends Comparator<Double> {
    @Override
    public int compare(double var1, double var3);

    @Override
    @Deprecated
    default public int compare(Double ok1, Double ok2) {
        return this.compare((double)ok1, (double)ok2);
    }
}

