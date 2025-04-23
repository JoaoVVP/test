/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSortedSet;

public abstract class AbstractLongSortedSet
extends AbstractLongSet
implements LongSortedSet {
    protected AbstractLongSortedSet() {
    }

    @Override
    public abstract LongBidirectionalIterator iterator();
}

