/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloatSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatSortedSet;

public abstract class AbstractFloatSortedSet
extends AbstractFloatSet
implements FloatSortedSet {
    protected AbstractFloatSortedSet() {
    }

    @Override
    public abstract FloatBidirectionalIterator iterator();
}

