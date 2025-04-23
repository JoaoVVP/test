/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteSortedSet;

public abstract class AbstractByteSortedSet
extends AbstractByteSet
implements ByteSortedSet {
    protected AbstractByteSortedSet() {
    }

    @Override
    public abstract ByteBidirectionalIterator iterator();
}

