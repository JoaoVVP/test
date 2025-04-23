/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSortedSet;

public abstract class AbstractDoubleSortedSet
extends AbstractDoubleSet
implements DoubleSortedSet {
    protected AbstractDoubleSortedSet() {
    }

    @Override
    public abstract DoubleBidirectionalIterator iterator();
}

