/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSortedSet;

public abstract class AbstractIntSortedSet
extends AbstractIntSet
implements IntSortedSet {
    protected AbstractIntSortedSet() {
    }

    @Override
    public abstract IntBidirectionalIterator iterator();
}

