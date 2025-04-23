/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import java.util.Comparator;

@FunctionalInterface
public interface IntComparator
extends Comparator<Integer> {
    @Override
    public int compare(int var1, int var2);

    @Override
    @Deprecated
    default public int compare(Integer ok1, Integer ok2) {
        return this.compare((int)ok1, (int)ok2);
    }
}

