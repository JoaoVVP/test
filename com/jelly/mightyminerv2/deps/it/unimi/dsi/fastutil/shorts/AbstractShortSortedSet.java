/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShortSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSortedSet;

public abstract class AbstractShortSortedSet
extends AbstractShortSet
implements ShortSortedSet {
    protected AbstractShortSortedSet() {
    }

    @Override
    public abstract ShortBidirectionalIterator iterator();
}

