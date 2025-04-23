/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BidirectionalIterator;

public interface BigListIterator<K>
extends BidirectionalIterator<K> {
    public long nextIndex();

    public long previousIndex();

    default public void set(K e) {
        throw new UnsupportedOperationException();
    }

    default public void add(K e) {
        throw new UnsupportedOperationException();
    }
}

