/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterable;

public interface LongBidirectionalIterable
extends LongIterable {
    @Override
    public LongBidirectionalIterator iterator();
}

