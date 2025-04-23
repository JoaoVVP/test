/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import java.util.Comparator;

@FunctionalInterface
public interface BooleanComparator
extends Comparator<Boolean> {
    @Override
    public int compare(boolean var1, boolean var2);

    @Override
    @Deprecated
    default public int compare(Boolean ok1, Boolean ok2) {
        return this.compare((boolean)ok1, (boolean)ok2);
    }
}

